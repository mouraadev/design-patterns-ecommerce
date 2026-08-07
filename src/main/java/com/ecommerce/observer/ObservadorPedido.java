package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.enums.StatusPedido;

/**
 * Padrão OBSERVER.
 * Define o contrato que interessados no ciclo de vida do pedido devem
 * implementar para serem notificados a cada mudança de status.
 */
public interface ObservadorPedido {
    void atualizar(Pedido pedido, StatusPedido novoStatus);
}
