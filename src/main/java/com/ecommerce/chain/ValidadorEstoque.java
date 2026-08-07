package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

public class ValidadorEstoque extends ValidadorPedido {

    @Override
    protected boolean executarValidacao(Pedido pedido) {
        boolean estoqueDisponivel = !pedido.getItens().isEmpty();
        System.out.println("[Validação] Verificando estoque... " + (estoqueDisponivel ? "OK" : "FALHOU"));
        return estoqueDisponivel;
    }
}
