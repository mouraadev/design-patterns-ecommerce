package com.ecommerce.factory;

public abstract class Produto {

    protected final String nome;
    protected final double preco;

    protected Produto(String nome, double preco) {
        this.nome = nome;
        this.preco = preco;
    }

    public String getNome() {
        return nome;
    }

    public double getPreco() {
        return preco;
    }

    public abstract String getCategoria();

    @Override
    public String toString() {
        return String.format("%s [%s] - R$ %.2f", nome, getCategoria(), preco);
    }
}
