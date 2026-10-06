package com.ecommerce.observer;

import com.ecommerce.builder.Order;
import com.ecommerce.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class EmailNotifier implements OrderObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(EmailNotifier.class);

    public EmailNotifier() {
    }

    @Override
    public void update(Order order, OrderStatus newStatus) {
        LOGGER.info("[Email] Hello {}, your order {} status changed to: {}",
                order.getCustomer(), order.getId(), newStatus);
    }
}
