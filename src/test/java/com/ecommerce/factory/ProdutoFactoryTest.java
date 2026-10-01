package com.ecommerce.factory;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProdutoFactoryTest {

    @Test
    void deveCriarCadaTipoDeProduto() {
        Produto eletronico = ProdutoFactory.criar(
                ProdutoFactory.TipoProduto.ELETRONICO, "Notebook", 3500.0);
        Produto livro = ProdutoFactory.criar(ProdutoFactory.TipoProduto.LIVRO, "Clean Code", 90.0);
        Produto roupa = ProdutoFactory.criar(ProdutoFactory.TipoProduto.ROUPA, "Camiseta", 60.0);

        assertInstanceOf(Eletronico.class, eletronico);
        assertEquals(12, ((Eletronico) eletronico).getGarantiaMeses());
        assertInstanceOf(Livro.class, livro);
        assertEquals("Livro", livro.getCategoria());
        assertInstanceOf(Roupa.class, roupa);
        assertEquals("Roupa", roupa.getCategoria());
    }

    @Test
    void deveRejeitarProdutoInvalido() {
        assertThrows(IllegalArgumentException.class,
                () -> ProdutoFactory.criar(null, "Produto", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> ProdutoFactory.criar(ProdutoFactory.TipoProduto.LIVRO, " ", 10.0));
        assertThrows(IllegalArgumentException.class,
                () -> ProdutoFactory.criar(ProdutoFactory.TipoProduto.LIVRO, "Livro", 0.0));
    }
}
