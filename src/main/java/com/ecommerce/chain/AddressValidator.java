package com.ecommerce.chain;

import com.ecommerce.builder.Order;
import com.ecommerce.exception.InvalidOrderException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class AddressValidator extends AbstractOrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressValidator.class);

    public AddressValidator() {
    }

    @Override
    protected void performValidation(Order order) {
        if (order.getDeliveryAddress() == null || order.getDeliveryAddress().isBlank()) {
            LOGGER.warn("Order {} has no delivery address", order.getId());
            throw new InvalidOrderException(
                    "The delivery address for order " + order.getId() + " is required.");
        }
        LOGGER.info("Address validated for order {}", order.getId());
    }
}
