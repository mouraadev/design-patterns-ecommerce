package com.ecommerce.factory;

public final class ProductFactory {

    private ProductFactory() {
    }

    public enum ProductType {

        ELECTRONICS,

        BOOK,

        CLOTHING
    }

    public static Product create(ProductType type, String name, double price) {
        if (type == null) {
            throw new IllegalArgumentException("The product type is required.");
        }
        return switch (type) {
            case ELECTRONICS -> new Electronics(name, price);
            case BOOK -> new Book(name, price);
            case CLOTHING -> new Clothing(name, price);
        };
    }
}
