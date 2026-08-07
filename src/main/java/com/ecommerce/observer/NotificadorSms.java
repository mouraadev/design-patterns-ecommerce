package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;

public class NotificadorSms implements ObservadorPedido {

    @Override
    public void atualizar(Pedido pedido, StatusPedido novoStatus) {
        System.out.printf("[SMS] %s, pedido %s: %s%n",
                pedido.getCliente(), pedido.getId(), novoStatus);
    }
}
