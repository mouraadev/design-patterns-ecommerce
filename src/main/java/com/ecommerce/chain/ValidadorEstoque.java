package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;
import com.ecommerce.exception.EstoqueInsuficienteException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ValidadorEstoque extends ValidadorPedidoAbstrato {

    private static final Logger LOGGER = LoggerFactory.getLogger(ValidadorEstoque.class);

    public ValidadorEstoque() {
    }

    @Override
    protected void executarValidacao(Pedido pedido) {
        if (pedido.getItens().isEmpty()) {
            LOGGER.warn("Pedido {} sem itens em estoque", pedido.getId());
            throw new EstoqueInsuficienteException(
                    "Não há itens disponíveis para o pedido " + pedido.getId() + ".");
        }
        LOGGER.info("Estoque validado para o pedido {}", pedido.getId());
    }
}
