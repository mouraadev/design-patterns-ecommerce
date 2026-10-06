package com.ecommerce.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductFactoryTest {

    @Test
    void shouldCreateEveryProductType() {
        Product electronics = ProductFactory.create(
                ProductFactory.ProductType.ELECTRONICS, "Notebook", 3500.0);
        Product book = ProductFactory.create(ProductFactory.ProductType.BOOK, "Clean Code", 90.0);
        Product clothing = ProductFactory.create(ProductFactory.ProductType.CLOTHING, "T-shirt", 60.0);

        assertInstanceOf(Electronics.class, electronics);
        assertEquals(12, ((Electronics) electronics).getWarrantyMonths());
        assertEquals("Electronics", electronics.getCategory());
        assertInstanceOf(Book.class, book);
        assertEquals("Book", book.getCategory());
        assertInstanceOf(Clothing.class, clothing);
        assertEquals("Clothing", clothing.getCategory());
    }

    @Test
    void shouldRejectInvalidProduct() {
        assertThrows(IllegalArgumentException.class,
                () -> ProductFactory.create(null, "Product", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> ProductFactory.create(ProductFactory.ProductType.BOOK, " ", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> ProductFactory.create(ProductFactory.ProductType.BOOK, "Book", 0.0));
    }
}
