package com.ecommerce.facade;

import com.ecommerce.builder.Order;
import com.ecommerce.chain.AddressValidator;
import com.ecommerce.chain.StockValidator;
import com.ecommerce.chain.FraudValidator;
import com.ecommerce.chain.OrderValidator;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.PaymentDeclinedException;
import com.ecommerce.exception.InvalidOrderException;
import com.ecommerce.observer.EmailNotifier;
import com.ecommerce.observer.SmsNotifier;
import com.ecommerce.observer.OrderObserver;
import com.ecommerce.singleton.SystemConfiguration;
import com.ecommerce.strategy.PaymentStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public final class OrderFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderFacade.class);

    private final SystemConfiguration config;
    private final OrderValidator validator;
    private final List<OrderObserver> observers;

    public OrderFacade() {
        this(SystemConfiguration.getInstance(), createDefaultChain(),
                List.of(new EmailNotifier(), new SmsNotifier()));
    }

    public OrderFacade(SystemConfiguration config, OrderValidator validator,
                        List<OrderObserver> observers) {
        this.config = Objects.requireNonNull(config, "The configuration is required.");
        this.validator = Objects.requireNonNull(validator, "The validator is required.");
        this.observers = List.copyOf(
                Objects.requireNonNull(observers, "The observer list is required."));
    }

    public void finishOrder(Order order, PaymentStrategy paymentStrategy) {
        Objects.requireNonNull(order, "The order is required.");
        Objects.requireNonNull(paymentStrategy, "The payment strategy is required.");
        order.claimProcessing();

        observers.forEach(order::addObserver);

        LOGGER.info("[{}] Starting order {} processing", config.getStoreName(), order.getId());

        try {
            validator.validate(order);
        } catch (InvalidOrderException exception) {
            order.updateStatus(OrderStatus.CANCELLED);
            LOGGER.warn("[{}] Order {} cancelled during validation: {}",
                    config.getStoreName(), order.getId(), exception.getMessage());
            throw exception;
        }

        LOGGER.info("[{}] Processing payment via {}", config.getStoreName(),
                paymentStrategy.getDescription());
        boolean paymentApproved = paymentStrategy.process(order.getTotalAmount());

        if (!paymentApproved) {
            order.updateStatus(OrderStatus.PAYMENT_DECLINED);
            throw new PaymentDeclinedException("Payment declined for order " + order.getId() + ".");
        }

        order.updateStatus(OrderStatus.PAYMENT_APPROVED);
        order.updateStatus(OrderStatus.PICKING);
        order.updateStatus(OrderStatus.SHIPPED);

        LOGGER.info("[{}] Order {} completed successfully", config.getStoreName(), order.getId());
    }

    private static OrderValidator createDefaultChain() {
        OrderValidator stock = new StockValidator();
        stock.setNext(new AddressValidator())
                .setNext(new FraudValidator());
        return stock;
    }
}
