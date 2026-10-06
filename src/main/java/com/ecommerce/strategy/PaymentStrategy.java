package com.ecommerce.strategy;

public interface PaymentStrategy {

    boolean process(double amount);

    String getDescription();
}
