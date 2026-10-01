package com.ecommerce.observer;

import com.ecommerce.builder.Pedido;
import com.ecommerce.decorator.Item;
import com.ecommerce.enums.StatusPedido;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

class ObserverTest {

    @Test
    void deveNotificarObservadorQuandoStatusMudar() {
        ObservadorPedido observador = mock(ObservadorPedido.class);
        Pedido pedido = pedidoValido();
        pedido.adicionarObservador(observador);

        pedido.atualizarStatus(StatusPedido.EM_SEPARACAO);

        verify(observador).atualizar(pedido, StatusPedido.EM_SEPARACAO);
    }

    private static Pedido pedidoValido() {
        Item item = mock(Item.class);
        return new Pedido.Builder("PED-OBS", "Ana")
                .comEndereco("Rua B, 20")
                .adicionarItem(item)
                .build();
    }
}
