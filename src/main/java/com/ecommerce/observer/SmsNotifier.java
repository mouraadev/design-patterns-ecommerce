package com.ecommerce.observer;

import com.ecommerce.builder.Order;
import com.ecommerce.enums.OrderStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class SmsNotifier implements OrderObserver {

    private static final Logger LOGGER = LoggerFactory.getLogger(SmsNotifier.class);

    public SmsNotifier() {
    }

    @Override
    public void update(Order order, OrderStatus newStatus) {
        LOGGER.info("[SMS] {}, order {}: {}", order.getCustomer(), order.getId(), newStatus);
    }
}
