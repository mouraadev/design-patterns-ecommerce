package com.ecommerce.factory;

import java.util.Locale;

public abstract class Product {

    private final String name;
    private final double price;

    protected Product(String name, double price) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("The product name is required.");
        }
        if (!Double.isFinite(price) || price <= 0) {
            throw new IllegalArgumentException("The product price must be positive.");
        }
        this.name = name.trim();
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public abstract String getCategory();

    @Override
    public String toString() {
        return String.format(Locale.US, "%s [%s] - R$ %.2f", name, getCategory(), price);
    }
}
