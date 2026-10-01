package com.ecommerce.strategy;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StrategyTest {

    @Test
    void deveProcessarTodasAsEstrategias() {
        EstrategiaPagamento cartao = new PagamentoCartaoCredito("1234567812345678", 3);
        EstrategiaPagamento boleto = new PagamentoBoleto();
        EstrategiaPagamento pix = new PagamentoPix();

        assertTrue(cartao.processar(150.0));
        assertEquals("Cartão de Crédito (3x)", cartao.getDescricao());
        assertTrue(boleto.processar(150.0));
        assertEquals("Boleto Bancário", boleto.getDescricao());
        assertTrue(pix.processar(150.0));
        assertEquals("Pix", pix.getDescricao());
    }

    @Test
    void deveRejeitarConfiguracaoEValoresInvalidos() {
        assertThrows(IllegalArgumentException.class,
                () -> new PagamentoCartaoCredito("abc", 1));
        assertThrows(IllegalArgumentException.class,
                () -> new PagamentoCartaoCredito("1234", 13));
        assertThrows(IllegalArgumentException.class, () -> new PagamentoPix().processar(0));
        assertThrows(IllegalArgumentException.class, () -> new PagamentoBoleto().processar(-1));
    }
}
