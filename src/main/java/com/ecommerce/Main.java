package com.ecommerce;

import com.ecommerce.builder.Order;
import com.ecommerce.decorator.GiftWrapping;
import com.ecommerce.decorator.Item;
import com.ecommerce.decorator.ProductItem;
import com.ecommerce.decorator.ShippingInsurance;
import com.ecommerce.facade.OrderFacade;
import com.ecommerce.factory.Product;
import com.ecommerce.factory.ProductFactory;
import com.ecommerce.strategy.PaymentStrategy;
import com.ecommerce.strategy.CreditCardPayment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class Main {

    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    private Main() {
    }

    public static void main(String[] args) {
        LOGGER.info("=== Order System - Design Patterns Demo ===");

        Product notebook = ProductFactory.create(ProductFactory.ProductType.ELECTRONICS, "Gaming Laptop", 4500.00);
        Product book = ProductFactory.create(ProductFactory.ProductType.BOOK, "Clean Code", 89.90);
        Product clothing = ProductFactory.create(ProductFactory.ProductType.CLOTHING, "DIO T-shirt", 59.90);

        Item notebookItem = new ShippingInsurance(new ProductItem(notebook));
        Item bookItem = new GiftWrapping(new ProductItem(book));
        Item clothingItem = new ProductItem(clothing);

        Order order = new Order.Builder("ORD-001", "Ryan Moura")
                .withAddress("123 Street - São Paulo/SP")
                .addItem(notebookItem)
                .addItem(bookItem)
                .addItem(clothingItem)
                .build();

        LOGGER.info("{}", order);

        PaymentStrategy payment = new CreditCardPayment("1234567812345678", 3);

        OrderFacade facade = new OrderFacade();
        facade.finishOrder(order, payment);

        LOGGER.info("Final order status: {}", order.getStatus());
    }
}
