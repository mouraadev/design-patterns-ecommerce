package com.ecommerce.strategy;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PagamentoCartaoCredito implements EstrategiaPagamento {

    private static final Logger LOGGER = LoggerFactory.getLogger(PagamentoCartaoCredito.class);

    private final String numeroCartao;
    private final int parcelas;

    public PagamentoCartaoCredito(String numeroCartao, int parcelas) {
        if (numeroCartao == null || !numeroCartao.matches("\\d{4,19}")) {
            throw new IllegalArgumentException("O cartão deve conter entre 4 e 19 dígitos.");
        }
        if (parcelas < 1 || parcelas > 12) {
            throw new IllegalArgumentException("A quantidade de parcelas deve estar entre 1 e 12.");
        }
        this.numeroCartao = numeroCartao;
        this.parcelas = parcelas;
    }

    @Override
    public boolean processar(double valor) {
        if (!Double.isFinite(valor) || valor <= 0) {
            throw new IllegalArgumentException("O valor do pagamento deve ser positivo.");
        }
        String final4 = numeroCartao.substring(numeroCartao.length() - 4);
        LOGGER.info("Cobrando R$ {} no cartão final {} em {}x",
                String.format("%.2f", valor), final4, parcelas);
        return true;
    }

    @Override
    public String getDescricao() {
        return "Cartão de Crédito (" + parcelas + "x)";
    }
}
