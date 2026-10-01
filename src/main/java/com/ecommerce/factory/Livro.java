package com.ecommerce.factory;

public final class Livro extends Produto {

    public Livro(String nome, double preco) {
        super(nome, preco);
    }

    @Override
    public String getCategoria() {
        return "Livro";
    }
}
