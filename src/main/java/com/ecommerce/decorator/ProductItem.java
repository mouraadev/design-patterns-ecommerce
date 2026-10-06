package com.ecommerce.decorator;

import com.ecommerce.factory.Product;

import java.util.Objects;

public final class ProductItem implements Item {

    private final Product product;

    public ProductItem(Product product) {
        this.product = Objects.requireNonNull(product, "The product is required.");
    }

    @Override
    public String getDescription() {
        return product.getName();
    }

    @Override
    public double getPrice() {
        return product.getPrice();
    }
}
