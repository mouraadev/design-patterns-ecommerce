package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;
import com.ecommerce.exception.PedidoInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ValidadorFraude extends ValidadorPedidoAbstrato {

    private static final double VALOR_SUSPEITO = 10_000.0;
    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadorFraude.class);

    public ValidadorFraude() {
    }

    @Override
    protected void executarValidacao(Pedido pedido) {
        if (pedido.getValorTotal() >= VALOR_SUSPEITO) {
            LOGGER.warn("Pedido {} encaminhado pela análise antifraude", pedido.getId());
            throw new PedidoInvalidoException(
                    "O pedido " + pedido.getId() + " requer análise antifraude manual.");
        }
        LOGGER.info("Análise antifraude aprovada para o pedido {}", pedido.getId());
    }
}
