package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Locale;

public final class CreditCardPayment implements PaymentStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(CreditCardPayment.class);

    private final String cardNumber;
    private final int installments;

    public CreditCardPayment(String cardNumber, int installments) {
        if (cardNumber == null || !cardNumber.matches("\\d{4,19}")) {
            throw new IllegalArgumentException("The card number must contain between 4 and 19 digits.");
        }
        if (installments < 1 || installments > 12) {
            throw new IllegalArgumentException("The number of installments must be between 1 and 12.");
        }
        this.cardNumber = cardNumber;
        this.installments = installments;
    }

    @Override
    public boolean process(double amount) {
        if (!Double.isFinite(amount) || amount <= 0) {
            throw new IllegalArgumentException("The payment amount must be positive.");
        }
        String lastFourDigits = cardNumber.substring(cardNumber.length() - 4);
        LOGGER.info("Charging R$ {} to the card ending in {} in {} installment(s)",
                String.format(Locale.US, "%.2f", amount), lastFourDigits, installments);
        return true;
    }

    @Override
    public String getDescription() {
        return "Credit Card (" + installments + "x)";
    }
}
