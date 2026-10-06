package com.ecommerce.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategyTest {

    @Test
    void shouldProcessAllStrategies() {
        PaymentStrategy card = new CreditCardPayment("1234567812345678", 3);
        PaymentStrategy bankSlip = new BankSlipPayment();
        PaymentStrategy pix = new PixPayment();

        assertTrue(card.process(150.0));
        assertEquals("Credit Card (3x)", card.getDescription());
        assertTrue(bankSlip.process(150.0));
        assertEquals("Bank Slip", bankSlip.getDescription());
        assertTrue(pix.process(150.0));
        assertEquals("Pix", pix.getDescription());
    }

    @Test
    void shouldRejectInvalidConfigurationAndAmounts() {
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCardPayment("abc", 1));
        assertThrows(IllegalArgumentException.class,
                () -> new CreditCardPayment("1234", 13));
        assertThrows(IllegalArgumentException.class, () -> new PixPayment().process(0));
        assertThrows(IllegalArgumentException.class, () -> new BankSlipPayment().process(-1));
    }
}
