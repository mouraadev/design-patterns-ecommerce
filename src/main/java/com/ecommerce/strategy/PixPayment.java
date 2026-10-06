package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class PixPayment implements PaymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(PixPayment.class);

    public PixPayment() {
    }

    @Override
    public boolean process(double amount) {
        validateAmount(amount);
        LOGGER.info("Generating a Pix QR code for R$ {}", String.format(Locale.US, "%.2f", amount));
        return true;
    }

    @Override
    public String getDescription() {
        return "Pix";
    }

    private static void validateAmount(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("The payment amount must be positive.");
        }
    }
}
