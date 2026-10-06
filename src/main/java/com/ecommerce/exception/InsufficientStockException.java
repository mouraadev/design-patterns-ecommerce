package com.ecommerce.exception;

public final class InsufficientStockException extends InvalidOrderException {
    public InsufficientStockException(String message) {
        super(message);
    }
}
