package com.ecommerce.chain;

import com.ecommerce.builder.Order;
import com.ecommerce.exception.InvalidOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class FraudValidator extends AbstractOrderValidator {

    private static final double SUSPICIOUS_AMOUNT_THRESHOLD = 10_000.0;
    private static final Logger LOGGER = LoggerFactory.getLogger(FraudValidator.class);

    public FraudValidator() {
    }

    @Override
    protected void performValidation(Order order) {
        if (order.getTotalAmount() >= SUSPICIOUS_AMOUNT_THRESHOLD) {
            LOGGER.warn("Order {} was flagged by fraud analysis", order.getId());
            throw new InvalidOrderException(
                    "Order " + order.getId() + " requires manual fraud analysis.");
        }
        LOGGER.info("Fraud analysis approved for order {}", order.getId());
    }
}
