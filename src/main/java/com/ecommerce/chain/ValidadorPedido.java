package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

/**
 * Padrão CHAIN OF RESPONSIBILITY.
 * Cada validador decide se trata a requisição ou a repassa ao próximo elo
 * da cadeia, permitindo compor regras de validação de forma flexível.
 */
public abstract class ValidadorPedido {

    private ValidadorPedido proximo;

    public ValidadorPedido definirProximo(ValidadorPedido proximo) {
        this.proximo = proximo;
        return proximo;
    }

    public boolean validar(Pedido pedido) {
        if (!executarValidacao(pedido)) {
            return false;
        }
        if (proximo != null) {
            return proximo.validar(pedido);
        }
        return true;
    }

    protected abstract boolean executarValidacao(Pedido pedido);
}
