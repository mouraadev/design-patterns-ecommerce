package com.ecommerce.factory;

public final class Roupa extends Produto {

    public Roupa(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Roupa";
    }
}
