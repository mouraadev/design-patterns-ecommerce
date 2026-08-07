package com.ecommerce.decorator;

import com.ecommerce.factory.Produto;


public class ProdutoItem implements Item {

    private final Produto produto;

    public ProdutoItem(Produto produto) {
        this.produto = produto;
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
