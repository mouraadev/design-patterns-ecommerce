package com.ecommerce.decorator;

public final class GiftWrapping extends ItemDecorator {

    private static final double WRAPPING_COST = 9.90;

    public GiftWrapping(Item decoratedItem) {
        super(decoratedItem);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Gift wrapping";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + WRAPPING_COST;
    }
}
