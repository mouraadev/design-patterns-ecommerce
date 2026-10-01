package com.ecommerce.factory;

public final class ProdutoFactory {

    private ProdutoFactory() {
    }

    public enum TipoProduto {

        ELETRONICO,

        LIVRO,

        ROUPA
    }

    public static Produto criar(TipoProduto tipo, String nome, double preco) {
        if (tipo == null) {
            throw new IllegalArgumentException("O tipo do produto é obrigatório.");
        }
        return switch (tipo) {
            case ELETRONICO -> new Eletronico(nome, preco);
            case LIVRO -> new Livro(nome, preco);
            case ROUPA -> new Roupa(nome, preco);
        };
    }
}
