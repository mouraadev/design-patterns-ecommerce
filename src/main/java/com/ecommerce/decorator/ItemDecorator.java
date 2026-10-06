package com.ecommerce.decorator;

import java.util.Objects;

public abstract class ItemDecorator implements Item {

    private final Item decoratedItem;

    protected ItemDecorator(Item decoratedItem) {
        this.decoratedItem = Objects.requireNonNull(decoratedItem, "The decorated item is required.");
    }

    @Override
    public double getPrice() {
        return decoratedItem.getPrice();
    }

    @Override
    public String getDescription() {
        return decoratedItem.getDescription();
    }
}
