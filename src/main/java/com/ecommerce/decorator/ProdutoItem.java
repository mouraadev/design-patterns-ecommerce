package com.ecommerce.decorator;

import com.ecommerce.factory.Produto;

import java.util.Objects;

public final class ProdutoItem implements Item {

    private final Produto produto;

    public ProdutoItem(Produto produto) {
        this.produto = Objects.requireNonNull(produto, "O produto é obrigatório.");
    }

    @Override
    public String getDescricao() {
        return produto.getNome();
    }

    @Override
    public double getPreco() {
        return produto.getPreco();
    }
}
