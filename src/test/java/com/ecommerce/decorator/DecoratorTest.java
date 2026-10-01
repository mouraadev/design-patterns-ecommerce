package com.ecommerce.decorator;

import com.ecommerce.factory.ProdutoFactory;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class DecoratorTest {

    @Test
    void deveComporDescricaoEPrecoSemAlterarProdutoOriginal() {
        Item original = new ProdutoItem(ProdutoFactory.criar(
                ProdutoFactory.TipoProduto.LIVRO, "Refactoring", 100.0));
        Item decorado = new SeguroEnvio(new EmbalagemPresente(original));

        assertEquals("Refactoring", original.getDescricao());
        assertEquals(100.0, original.getPreco(), 0.001);
        assertEquals("Refactoring + Embalagem para presente + Seguro de envio",
                decorado.getDescricao());
        assertEquals(134.80, decorado.getPreco(), 0.001);
    }

    @Test
    void deveRejeitarDependenciasNulas() {
        assertThrows(NullPointerException.class, () -> new ProdutoItem(null));
        assertThrows(NullPointerException.class, () -> new SeguroEnvio(null));
    }
}
