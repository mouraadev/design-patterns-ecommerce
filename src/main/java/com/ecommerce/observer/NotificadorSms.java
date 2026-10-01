package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class NotificadorSms implements ObservadorPedido {

    private static final Logger LOGGER = LoggerFactory.getLogger(NotificadorSms.class);

    public NotificadorSms() {
    }

    @Override
    public void atualizar(Pedido pedido, StatusPedido novoStatus) {
        LOGGER.info("[SMS] {}, pedido {}: {}", pedido.getCliente(), pedido.getId(), novoStatus);
    }
}
