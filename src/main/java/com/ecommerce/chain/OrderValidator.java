package com.ecommerce.chain;

import com.ecommerce.builder.Order;

public interface OrderValidator {

    OrderValidator setNext(OrderValidator next);

    void validate(Order order);
}
