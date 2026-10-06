package com.ecommerce.singleton;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SingletonTest {

    @Test
    void shouldAlwaysReturnSameInstanceIncludingInParallel() {
        SystemConfiguration expected = SystemConfiguration.getInstance();

        IntStream.range(0, 100).parallel()
                .forEach(index -> assertSame(expected, SystemConfiguration.getInstance()));

        assertEquals("TechShop Brazil", expected.getStoreName());
        assertEquals(0.08, expected.getDefaultTaxRate(), 0.001);
    }
}
