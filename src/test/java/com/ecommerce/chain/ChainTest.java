package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;
import com.ecommerce.decorator.Item;
import com.ecommerce.exception.EstoqueInsuficienteException;
import com.ecommerce.exception.PedidoInvalidoException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class ChainTest {

    @Test
    void deveExecutarTodaACadeia() {
        Pedido pedido = pedidoCom("Rua C, 30", 100.0);
        ValidadorPedido proximo = mock(ValidadorPedido.class);
        ValidadorPedido endereco = new ValidadorEndereco();
        endereco.definirProximo(proximo);

        assertDoesNotThrow(() -> endereco.validar(pedido));
        verify(proximo).validar(pedido);
    }

    @Test
    void deveLancarExcecaoParaPedidoSemEstoque() {
        Pedido pedido = mock(Pedido.class);
        when(pedido.getId()).thenReturn("PED-EMPTY");
        when(pedido.getItens()).thenReturn(List.of());

        assertThrows(EstoqueInsuficienteException.class,
                () -> new ValidadorEstoque().validar(pedido));
    }

    @Test
    void deveLancarExcecaoParaEnderecoAusenteOuSuspeitaDeFraude() {
        assertThrows(PedidoInvalidoException.class,
                () -> new ValidadorEndereco().validar(pedidoCom(null, 100.0)));
        assertThrows(PedidoInvalidoException.class,
                () -> new ValidadorFraude().validar(pedidoCom("Rua D, 40", 10_000.0)));
    }

    private static Pedido pedidoCom(String endereco, double preco) {
        Item item = mock(Item.class);
        when(item.getPreco()).thenReturn(preco);
        Pedido.Builder builder = new Pedido.Builder("PED-CHAIN", "Bia").adicionarItem(item);
        if (endereco != null) {
            builder.comEndereco(endereco);
        }
        return builder.build();
    }
}
