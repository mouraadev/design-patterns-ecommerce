package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;

public interface ObservadorPedido {

    void atualizar(Pedido pedido, StatusPedido novoStatus);
}
