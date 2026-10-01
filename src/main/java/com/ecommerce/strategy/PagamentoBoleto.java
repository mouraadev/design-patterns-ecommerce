package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PagamentoBoleto implements EstrategiaPagamento {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoBoleto.class);

    public PagamentoBoleto() {
    }

    @Override
    public boolean processar(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
        LOGGER.info("Gerando boleto no valor de R$ {} (vencimento em 3 dias úteis)",
                String.format("%.2f", valor));
        return true;
    }

    @Override
    public String getDescricao() {
        return "Boleto Bancário";
    }
}
