package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class BankSlipPayment implements PaymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(BankSlipPayment.class);

    public BankSlipPayment() {
    }

    @Override
    public boolean process(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("The payment amount must be positive.");
        }
        LOGGER.info("Generating a bank slip for R$ {} (due in 3 business days)",
                String.format(Locale.US, "%.2f", amount));
        return true;
    }

    @Override
    public String getDescription() {
        return "Bank Slip";
    }
}
