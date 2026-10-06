package com.ecommerce.observer;

import com.ecommerce.builder.Order;
import com.ecommerce.enums.OrderStatus;

public interface OrderObserver {

    void update(Order order, OrderStatus newStatus);
}
