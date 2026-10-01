package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;
import com.ecommerce.exception.PedidoInvalidoException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ValidadorEndereco extends ValidadorPedidoAbstrato {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadorEndereco.class);

    public ValidadorEndereco() {
    }

    @Override
    protected void executarValidacao(Pedido pedido) {
        if (pedido.getEnderecoEntrega() == null || pedido.getEnderecoEntrega().isBlank()) {
            LOGGER.warn("Pedido {} sem endereço de entrega", pedido.getId());
            throw new PedidoInvalidoException(
                    "O endereço de entrega do pedido " + pedido.getId() + " é obrigatório.");
        }
        LOGGER.info("Endereço validado para o pedido {}", pedido.getId());
    }
}
