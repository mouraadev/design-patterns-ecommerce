package com.ecommerce.observer;

import com.ecommerce.builder.Order;
import com.ecommerce.decorator.Item;
import com.ecommerce.enums.OrderStatus;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ObserverTest {

    @Test
    void shouldNotifyObserverWhenStatusChanges() {
        OrderObserver observer = mock(OrderObserver.class);
        Order order = validOrder();
        order.addObserver(observer);

        order.updateStatus(OrderStatus.PICKING);

        verify(observer).update(order, OrderStatus.PICKING);
    }

    @Test
    void shouldPreserveEventStatusWhenAnotherObserverChangesTheOrder() {
        Order order = validOrder();
        List<OrderStatus> events = new ArrayList<>();
        order.addObserver((changedOrder, status) -> {
            if (status == OrderStatus.PAYMENT_APPROVED) {
                changedOrder.updateStatus(OrderStatus.PICKING);
            }
        });
        order.addObserver((changedOrder, status) -> events.add(status));

        order.updateStatus(OrderStatus.PAYMENT_APPROVED);

        assertEquals(List.of(OrderStatus.PICKING, OrderStatus.PAYMENT_APPROVED), events);
        assertEquals(OrderStatus.PICKING, order.getStatus());
    }

    private static Order validOrder() {
        Item item = mock(Item.class);
        return new Order.Builder("ORD-OBS", "Anna")
                .withAddress("20 B Street")
                .addItem(item)
                .build();
    }
}
