package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

public class ValidadorEndereco extends ValidadorPedido {

    @Override
    protected boolean executarValidacao(Pedido pedido) {
        boolean valido = pedido.getEnderecoEntrega() != null && !pedido.getEnderecoEntrega().isBlank();
        System.out.println("[Validação] Verificando endereço de entrega... " + (valido ? "OK" : "FALHOU"));
        return valido;
    }
}
