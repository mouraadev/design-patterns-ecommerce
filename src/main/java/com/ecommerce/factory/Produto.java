package com.ecommerce.factory;

public abstract class Produto {

    private final String nome;
    private final double preco;

    protected Produto(String nome, double preco) {
        if (nome == null || nome.isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (!Double.isFinite(preco) || preco <= 0) {
            throw new IllegalArgumentException("O preço do produto deve ser positivo.");
        }
        this.nome = nome.trim();
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
