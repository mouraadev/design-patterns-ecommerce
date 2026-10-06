package com.ecommerce.facade;

import com.ecommerce.builder.Order;
import com.ecommerce.chain.OrderValidator;
import com.ecommerce.decorator.Item;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.PaymentDeclinedException;
import com.ecommerce.exception.InvalidOrderException;
import com.ecommerce.observer.OrderObserver;
import com.ecommerce.singleton.SystemConfiguration;
import com.ecommerce.strategy.PaymentStrategy;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class FacadeTest {

    @Test
    void shouldOrchestrateValidationPaymentAndNotifications() {
        OrderValidator validator = mock(OrderValidator.class);
        OrderObserver observer = mock(OrderObserver.class);
        PaymentStrategy payment = mock(PaymentStrategy.class);
        Order order = validOrder();
        when(payment.getDescription()).thenReturn("Test");
        when(payment.process(50.0)).thenReturn(true);
        OrderFacade facade = facade(validator, observer);

        facade.finishOrder(order, payment);

        verify(validator).validate(order);
        verify(payment).process(50.0);
        ArgumentCaptor<OrderStatus> status = ArgumentCaptor.forClass(OrderStatus.class);
        verify(observer, times(3)).update(org.mockito.ArgumentMatchers.eq(order), status.capture());
        assertEquals(List.of(OrderStatus.PAYMENT_APPROVED,
                OrderStatus.PICKING, OrderStatus.SHIPPED), status.getAllValues());
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
    }

    @Test
    void shouldCancelOrderWhenValidationFails() {
        OrderValidator validator = mock(OrderValidator.class);
        OrderObserver observer = mock(OrderObserver.class);
        Order order = validOrder();
        InvalidOrderException failure = new InvalidOrderException("Invalid");
        PaymentStrategy payment = mock(PaymentStrategy.class);
        doThrow(failure).when(validator).validate(order);

        InvalidOrderException received = assertThrows(InvalidOrderException.class,
                () -> facade(validator, observer).finishOrder(
                        order, payment));

        assertEquals(failure, received);
        assertEquals(OrderStatus.CANCELLED, order.getStatus());
        verify(observer).update(order, OrderStatus.CANCELLED);
        verifyNoInteractions(payment);
    }

    @Test
    void shouldMarkPaymentDeclinedAndThrowException() {
        OrderValidator validator = mock(OrderValidator.class);
        OrderObserver observer = mock(OrderObserver.class);
        PaymentStrategy payment = mock(PaymentStrategy.class);
        Order order = validOrder();
        when(payment.getDescription()).thenReturn("Test");
        when(payment.process(50.0)).thenReturn(false);

        assertThrows(PaymentDeclinedException.class,
                () -> facade(validator, observer).finishOrder(order, payment));

        assertEquals(OrderStatus.PAYMENT_DECLINED, order.getStatus());
        verify(observer).update(order, OrderStatus.PAYMENT_DECLINED);
    }

    @Test
    void shouldRejectConcurrentProcessingAcrossFacadeInstances() throws Exception {
        Order order = validOrder();
        OrderValidator validator = mock(OrderValidator.class);
        PaymentStrategy payment = mock(PaymentStrategy.class);
        CountDownLatch paymentStarted = new CountDownLatch(1);
        CountDownLatch releasePayment = new CountDownLatch(1);
        when(payment.process(50.0)).thenAnswer(invocation -> {
            paymentStarted.countDown();
            assertTrue(releasePayment.await(5, TimeUnit.SECONDS));
            return true;
        });
        OrderFacade firstFacade = new OrderFacade(SystemConfiguration.getInstance(), validator, List.of());
        OrderFacade secondFacade = new OrderFacade(SystemConfiguration.getInstance(), validator, List.of());
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            Future<?> first = executor.submit(() -> firstFacade.finishOrder(order, payment));
            assertTrue(paymentStarted.await(5, TimeUnit.SECONDS));
            Future<?> second = executor.submit(() -> assertThrows(InvalidOrderException.class,
                    () -> secondFacade.finishOrder(order, payment)));
            second.get(5, TimeUnit.SECONDS);
            releasePayment.countDown();
            first.get(5, TimeUnit.SECONDS);

            verify(payment).process(50.0);
            verify(validator).validate(order);
            assertEquals(OrderStatus.SHIPPED, order.getStatus());
        } finally {
            releasePayment.countDown();
            executor.shutdownNow();
        }
    }

    @Test
    void shouldRejectReprocessingEvenIfStatusIsReset() {
        Order order = validOrder();
        PaymentStrategy payment = mock(PaymentStrategy.class);
        when(payment.process(50.0)).thenReturn(true);
        OrderFacade facade = facade(mock(OrderValidator.class), mock(OrderObserver.class));

        facade.finishOrder(order, payment);
        assertThrows(InvalidOrderException.class, () -> facade.finishOrder(order, payment));
        order.updateStatus(OrderStatus.CREATED);
        assertThrows(InvalidOrderException.class, () -> facade.finishOrder(order, payment));

        verify(payment).process(50.0);
    }

    @Test
    void shouldNotRetryPaymentAfterUnexpectedFailure() {
        Order order = validOrder();
        PaymentStrategy payment = mock(PaymentStrategy.class);
        when(payment.process(50.0)).thenThrow(new IllegalStateException("Unknown payment outcome"));
        OrderFacade facade = facade(mock(OrderValidator.class), mock(OrderObserver.class));

        assertThrows(IllegalStateException.class, () -> facade.finishOrder(order, payment));
        assertThrows(InvalidOrderException.class, () -> facade.finishOrder(order, payment));

        verify(payment).process(50.0);
    }

    @Test
    void shouldFinishAndNotifyOtherObserversWhenOneFails() {
        Order order = validOrder();
        PaymentStrategy payment = mock(PaymentStrategy.class);
        when(payment.process(50.0)).thenReturn(true);
        OrderObserver failing = mock(OrderObserver.class);
        OrderObserver healthy = mock(OrderObserver.class);
        doThrow(new IllegalStateException("Notifier unavailable")).when(failing).update(any(), any());
        OrderFacade facade = new OrderFacade(SystemConfiguration.getInstance(),
                mock(OrderValidator.class), List.of(failing, healthy));

        facade.finishOrder(order, payment);

        ArgumentCaptor<OrderStatus> statuses = ArgumentCaptor.forClass(OrderStatus.class);
        verify(healthy, times(3)).update(org.mockito.ArgumentMatchers.eq(order), statuses.capture());
        assertEquals(List.of(OrderStatus.PAYMENT_APPROVED, OrderStatus.PICKING, OrderStatus.SHIPPED),
                statuses.getAllValues());
        assertEquals(OrderStatus.SHIPPED, order.getStatus());
        verify(payment).process(50.0);
    }

    private static OrderFacade facade(OrderValidator validator, OrderObserver observer) {
        return new OrderFacade(SystemConfiguration.getInstance(), validator, List.of(observer));
    }

    private static Order validOrder() {
        Item item = mock(Item.class);
        when(item.getPrice()).thenReturn(50.0);
        return new Order.Builder("ORD-FACADE", "Kyle")
                .withAddress("50 E Street")
                .addItem(item)
                .build();
    }
}
