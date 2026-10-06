package com.ecommerce.chain;

import com.ecommerce.builder.Order;
import com.ecommerce.exception.InsufficientStockException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class StockValidator extends AbstractOrderValidator {

    private static final Logger LOGGER = LoggerFactory.getLogger(StockValidator.class);

    public StockValidator() {
    }

    @Override
    protected void performValidation(Order order) {
        if (order.getItems().isEmpty()) {
            LOGGER.warn("Order {} has no items in stock", order.getId());
            throw new InsufficientStockException(
                    "No items are available for order " + order.getId() + ".");
        }
        LOGGER.info("Stock validated for order {}", order.getId());
    }
}
