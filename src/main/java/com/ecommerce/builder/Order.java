package com.ecommerce.builder;

import com.ecommerce.decorator.Item;
import com.ecommerce.enums.OrderStatus;
import com.ecommerce.exception.InvalidOrderException;
import com.ecommerce.observer.OrderObserver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public final class Order {

    private static final Logger LOGGER = LoggerFactory.getLogger(Order.class);

    private final String id;
    private final String customer;
    private final String deliveryAddress;
    private final List<Item> items;
    private volatile OrderStatus status;
    private final List<OrderObserver> observers = new CopyOnWriteArrayList<>();
    private final AtomicBoolean processingStarted = new AtomicBoolean();

    private Order(Builder builder) {
        this.id = builder.id;
        this.customer = builder.customer;
        this.deliveryAddress = builder.deliveryAddress;
        this.items = List.copyOf(builder.items);
        this.status = OrderStatus.CREATED;
    }

    public String getId() {
        return id;
    }

    public String getCustomer() {
        return customer;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public List<Item> getItems() {
        return items;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getTotalAmount() {
        return items.stream().mapToDouble(Item::getPrice).sum();
    }

    public void addObserver(OrderObserver observer) {
        observers.add(Objects.requireNonNull(observer, "The observer is required."));
    }

    public void updateStatus(OrderStatus newStatus) {
        this.status = Objects.requireNonNull(newStatus, "The new status is required.");
        // Keep the event's status even if a listener updates the order again.
        notifyObservers(newStatus);
    }

    public void claimProcessing() {
        // Never release this claim after a failure: the payment outcome may be unknown.
        if (status != OrderStatus.CREATED || !processingStarted.compareAndSet(false, true)) {
            throw new InvalidOrderException("Only an unprocessed order with CREATED status can be completed.");
        }
    }

    private void notifyObservers(OrderStatus newStatus) {
        for (OrderObserver observer : observers) {
            try {
                observer.update(this, newStatus);
            } catch (RuntimeException exception) {
                // Notification failures must not interrupt an already paid order or other listeners.
                LOGGER.warn("Observer {} failed for order {} at status {}",
                        observer.getClass().getName(), id, newStatus, exception);
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order ").append(id).append(" - Customer: ").append(customer).append("\n");
        for (Item item : items) {
            sb.append("  - ").append(item.getDescription())
              .append(" (R$ ").append(String.format(Locale.US, "%.2f", item.getPrice())).append(")\n");
        }
        sb.append("Total: R$ ").append(String.format(Locale.US, "%.2f", getTotalAmount())).append("\n");
        sb.append("Status: ").append(status);
        return sb.toString();
    }

    public static class Builder {
        private final String id;
        private final String customer;
        private String deliveryAddress;
        private final List<Item> items = new ArrayList<>();

        public Builder(String id, String customer) {
            this.id = requireText(id, "The order ID is required.");
            this.customer = requireText(customer, "The customer is required.");
        }

        public Builder withAddress(String address) {
            this.deliveryAddress = requireText(address, "The delivery address is required.");
            return this;
        }

        public Builder addItem(Item item) {
            this.items.add(Objects.requireNonNull(item, "The item is required."));
            return this;
        }

        public Order build() {
            if (items.isEmpty()) {
                throw new InvalidOrderException("The order must contain at least one item.");
            }
            return new Order(this);
        }

        private static String requireText(String value, String message) {
            if (value == null || value.isBlank()) {
                throw new InvalidOrderException(message);
            }
            return value.trim();
        }
    }
}
