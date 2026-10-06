package com.ecommerce.builder;

import com.ecommerce.decorator.Item;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.InvalidOrderException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderBuilderTest {

    private static final Item ITEM = new Item() {
        @Override
        public String getDescription() {
            return "Test product";
        }

        @Override
        public double getPrice() {
            return 25.50;
        }
    };

    @Test
    void shouldBuildOrderWithImmutableDataAndItems() {
        Order.Builder builder = new Order.Builder(" ORD-1 ", " Maria ")
                .withAddress(" 10 A Street ")
                .addItem(ITEM);

        Order order = builder.build();
        builder.addItem(ITEM);

        assertEquals("ORD-1", order.getId());
        assertEquals("Maria", order.getCustomer());
        assertEquals("10 A Street", order.getDeliveryAddress());
        assertEquals(OrderStatus.CREATED, order.getStatus());
        assertEquals(1, order.getItems().size());
        assertEquals(25.50, order.getTotalAmount(), 0.001);
        assertThrows(UnsupportedOperationException.class, () -> order.getItems().add(ITEM));
    }

    @Test
    void shouldRejectOrderWithoutItems() {
        Order.Builder builder = new Order.Builder("ORD-2", "John");

        assertThrows(InvalidOrderException.class, builder::build);
    }

    @Test
    void shouldRejectInvalidRequiredFields() {
        assertThrows(InvalidOrderException.class, () -> new Order.Builder(" ", "Customer"));
        assertThrows(InvalidOrderException.class, () -> new Order.Builder("ORD", null));
        assertThrows(NullPointerException.class,
                () -> new Order.Builder("ORD", "Customer").addItem(null));
    }
}
