package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotificadorEmail implements ObservadorPedido {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificadorEmail.class);

    public NotificadorEmail() {
    }

    @Override
    public void atualizar(Pedido pedido, StatusPedido novoStatus) {
        LOGGER.info("[E-mail] Olá {}, o status do seu pedido {} mudou para: {}",
                pedido.getCliente(), pedido.getId(), novoStatus);
    }
}
