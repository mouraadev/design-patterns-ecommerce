package com.ecommerce.factory;

public final class Clothing extends Product {

    public Clothing(String name, double price) {
        super(name, price);
    }

    @Override
    public String getCategory() {
        return "Clothing";
    }
}
