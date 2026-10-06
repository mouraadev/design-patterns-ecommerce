package com.ecommerce.factory;

public final class Electronics extends Product {

    private final int warrantyMonths;

    public Electronics(String name, double price) {
        super(name, price);
        this.warrantyMonths = 12;
    }

    public int getWarrantyMonths() {
        return warrantyMonths;
    }

    @Override
    public String getCategory() {
        return "Electronics";
    }
}
