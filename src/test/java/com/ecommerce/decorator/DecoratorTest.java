package com.ecommerce.decorator;

import com.ecommerce.factory.ProductFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecoratorTest {

    @Test
    void shouldComposeDescriptionAndPriceWithoutChangingOriginalProduct() {
        Item original = new ProductItem(ProductFactory.create(
                ProductFactory.ProductType.BOOK, "Refactoring", 100.0));
        Item decorated = new ShippingInsurance(new GiftWrapping(original));

        assertEquals("Refactoring", original.getDescription());
        assertEquals(100.0, original.getPrice(), 0.001);
        assertEquals("Refactoring + Gift wrapping + Shipping insurance",
                decorated.getDescription());
        assertEquals(134.80, decorated.getPrice(), 0.001);
    }

    @Test
    void shouldRejectNullDependencies() {
        assertThrows(NullPointerException.class, () -> new ProductItem(null));
        assertThrows(NullPointerException.class, () -> new ShippingInsurance(null));
    }
}
