package com.ecommerce.chain;

import com.ecommerce.builder.Order;

import java.util.Objects;

public abstract class AbstractOrderValidator implements OrderValidator {

    private OrderValidator next;

    protected AbstractOrderValidator() {
    }

    @Override
    public final OrderValidator setNext(OrderValidator next) {
        this.next = Objects.requireNonNull(next, "The next validator is required.");
        return next;
    }

    @Override
    public final void validate(Order order) {
        performValidation(Objects.requireNonNull(order, "The order is required."));
        if (next != null) {
            next.validate(order);
        }
    }

    protected abstract void performValidation(Order order);
}
