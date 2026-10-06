package com.ecommerce.chain;

import com.ecommerce.builder.Order;
import com.ecommerce.decorator.Item;
import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.exception.InvalidOrderException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

class ChainTest {

    @Test
    void shouldInvokeNextValidator() {
        Order order = orderWith("30 C Street", 100.0);
        OrderValidator next = mock(OrderValidator.class);
        OrderValidator address = new AddressValidator();
        address.setNext(next);

        assertDoesNotThrow(() -> address.validate(order));
        verify(next).validate(order);
    }

    @Test
    void shouldThrowWhenOrderHasNoStock() {
        Order order = mock(Order.class);
        when(order.getId()).thenReturn("ORD-EMPTY");
        when(order.getItems()).thenReturn(List.of());

        assertThrows(InsufficientStockException.class,
                () -> new StockValidator().validate(order));
    }

    @Test
    void shouldThrowForMissingAddressOrSuspectedFraud() {
        assertThrows(InvalidOrderException.class,
                () -> new AddressValidator().validate(orderWith(null, 100.0)));
        assertThrows(InvalidOrderException.class,
                () -> new FraudValidator().validate(orderWith("40 D Street", 10_000.0)));
    }

    @Test
    void shouldStopBeforeNextValidatorWhenCurrentRuleFails() {
        OrderValidator next = mock(OrderValidator.class);
        OrderValidator address = new AddressValidator();
        address.setNext(next);

        assertThrows(InvalidOrderException.class, () -> address.validate(orderWith(null, 100.0)));

        verifyNoInteractions(next);
    }

    @Test
    void shouldAcceptOrderThroughAllDefaultRules() {
        OrderValidator stock = new StockValidator();
        stock.setNext(new AddressValidator()).setNext(new FraudValidator());

        assertDoesNotThrow(() -> stock.validate(orderWith("Valid address", 9_999.99)));
    }

    private static Order orderWith(String address, double price) {
        Item item = mock(Item.class);
        when(item.getPrice()).thenReturn(price);
        Order.Builder builder = new Order.Builder("ORD-CHAIN", "Bea").addItem(item);
        if (address != null) {
            builder.withAddress(address);
        }
        return builder.build();
    }
}
