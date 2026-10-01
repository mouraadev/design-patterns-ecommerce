package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PagamentoPix implements EstrategiaPagamento {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoPix.class);

    public PagamentoPix() {
    }

    @Override
    public boolean processar(double valor) {
        validarValor(valor);
        LOGGER.info("Gerando QR Code Pix no valor de R$ {}", String.format("%.2f", valor));
        return true;
    }

    @Override
    public String getDescricao() {
        return "Pix";
    }

    private static void validarValor(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
    }
}
