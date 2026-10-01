package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

import java.util.Objects;

public abstract class ValidadorPedidoAbstrato implements ValidadorPedido {

    private ValidadorPedido proximo;

    protected ValidadorPedidoAbstrato() {
    }

    @Override
    public final ValidadorPedido definirProximo(ValidadorPedido proximo) {
        this.proximo = Objects.requireNonNull(proximo, "O próximo validador é obrigatório.");
        return proximo;
    }

    @Override
    public final void validar(Pedido pedido) {
        executarValidacao(Objects.requireNonNull(pedido, "O pedido é obrigatório."));
        if (proximo != null) {
            proximo.validar(pedido);
        }
    }

    protected abstract void executarValidacao(Pedido pedido);
}
