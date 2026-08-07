package com.ecommerce.factory;

/**
 * Padrão FACTORY METHOD.
 * Centraliza a criação de diferentes tipos de {@link Produto}, escondendo do
 * cliente qual classe concreta será instanciada.
 */
public class ProdutoFactory {

    public enum TipoProduto {
        ELETRONICO,
        LIVRO,
        ROUPA
    }

    public static Produto criar(TipoProduto tipo, String nome, double preco) {
        return switch (tipo) {
            case ELETRONICO -> new Eletronico(nome, preco);
            case LIVRO -> new Livro(nome, preco);
            case ROUPA -> new Roupa(nome, preco);
        };
    }
}
