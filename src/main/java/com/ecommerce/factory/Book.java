package com.ecommerce.factory;

public final class Book extends Product {

    public Book(String name, double price) {
        super(name, price);
    }

    @Override
    public String getCategory() {
        return "Book";
    }
}
