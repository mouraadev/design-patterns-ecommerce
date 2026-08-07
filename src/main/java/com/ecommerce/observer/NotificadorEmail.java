package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;

public class NotificadorEmail implements ObservadorPedido {

    @Override
    public void atualizar(Pedido pedido, StatusPedido novoStatus) {
        System.out.printf("[E-mail] Olá %s, o status do seu pedido %s mudou para: %s%n",
                pedido.getCliente(), pedido.getId(), novoStatus);
    }
}
