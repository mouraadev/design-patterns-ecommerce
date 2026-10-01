package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

public interface ValidadorPedido {

    ValidadorPedido definirProximo(ValidadorPedido proximo);

    void validar(Pedido pedido);
}
