package com.ecommerce.builder;

import com.ecommerce.decorator.Item;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.PedidoInvalidoException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PedidoBuilderTest {

    private static final Item ITEM = new Item() {
        @Override
        public String getDescricao() {
            return "Produto de teste";
        }

        @Override
        public double getPreco() {
            return 25.50;
        }
    };

    @Test
    void deveConstruirPedidoComDadosEItensImutaveis() {
        Pedido.Builder builder = new Pedido.Builder(" PED-1 ", " Maria ")
                .comEndereco(" Rua A, 10 ")
                .adicionarItem(ITEM);

        Pedido pedido = builder.build();
        builder.adicionarItem(ITEM);

        assertEquals("PED-1", pedido.getId());
        assertEquals("Maria", pedido.getCliente());
        assertEquals("Rua A, 10", pedido.getEnderecoEntrega());
        assertEquals(StatusPedido.CRIADO, pedido.getStatus());
        assertEquals(1, pedido.getItens().size());
        assertEquals(25.50, pedido.getValorTotal(), 0.001);
        assertThrows(UnsupportedOperationException.class, () -> pedido.getItens().add(ITEM));
    }

    @Test
    void deveRejeitarPedidoSemItens() {
        Pedido.Builder builder = new Pedido.Builder("PED-2", "João");

        assertThrows(PedidoInvalidoException.class, builder::build);
    }

    @Test
    void deveRejeitarDadosObrigatoriosInvalidos() {
        assertThrows(PedidoInvalidoException.class, () -> new Pedido.Builder(" ", "Cliente"));
        assertThrows(PedidoInvalidoException.class, () -> new Pedido.Builder("PED", null));
        assertThrows(NullPointerException.class,
                () -> new Pedido.Builder("PED", "Cliente").adicionarItem(null));
    }
}
