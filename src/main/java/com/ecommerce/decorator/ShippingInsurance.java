package com.ecommerce.decorator;

public final class ShippingInsurance extends ItemDecorator {

    private static final double INSURANCE_COST = 24.90;

    public ShippingInsurance(Item decoratedItem) {
        super(decoratedItem);
    }

    @Override
    public String getDescription() {
        return super.getDescription() + " + Shipping insurance";
    }

    @Override
    public double getPrice() {
        return super.getPrice() + INSURANCE_COST;
    }
}
