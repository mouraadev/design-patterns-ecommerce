package com.ecommerce.facade;

import com.ecommerce.builder.Pedido;
import com.ecommerce.chain.ValidadorPedido;
import com.ecommerce.decorator.Item;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.PagamentoRecusadoException;
import com.ecommerce.exception.PedidoInvalidoException;
import com.ecommerce.observer.ObservadorPedido;
import com.ecommerce.singleton.ConfiguracaoSistema;
import com.ecommerce.strategy.EstrategiaPagamento;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class FacadeTest {

    @Test
    void deveOrquestrarValidacaoPagamentoENotificacoes() {
        ValidadorPedido validador = mock(ValidadorPedido.class);
        ObservadorPedido observador = mock(ObservadorPedido.class);
        EstrategiaPagamento pagamento = mock(EstrategiaPagamento.class);
        Pedido pedido = pedidoValido();
        when(pagamento.getDescricao()).thenReturn("Teste");
        when(pagamento.processar(50.0)).thenReturn(true);
        PedidoFacade facade = facade(validador, observador);

        facade.finalizarPedido(pedido, pagamento);

        verify(validador).validar(pedido);
        verify(pagamento).processar(50.0);
        ArgumentCaptor<StatusPedido> status = ArgumentCaptor.forClass(StatusPedido.class);
        verify(observador, times(3)).atualizar(org.mockito.ArgumentMatchers.eq(pedido), status.capture());
        assertEquals(List.of(StatusPedido.PAGAMENTO_APROVADO,
                StatusPedido.EM_SEPARACAO, StatusPedido.ENVIADO), status.getAllValues());
        assertEquals(StatusPedido.ENVIADO, pedido.getStatus());
    }

    @Test
    void deveCancelarPedidoQuandoValidacaoFalhar() {
        ValidadorPedido validador = mock(ValidadorPedido.class);
        ObservadorPedido observador = mock(ObservadorPedido.class);
        Pedido pedido = pedidoValido();
        PedidoInvalidoException falha = new PedidoInvalidoException("Inválido");
        doThrow(falha).when(validador).validar(pedido);

        PedidoInvalidoException recebida = assertThrows(PedidoInvalidoException.class,
                () -> facade(validador, observador).finalizarPedido(
                        pedido, mock(EstrategiaPagamento.class)));

        assertEquals(falha, recebida);
        assertEquals(StatusPedido.CANCELADO, pedido.getStatus());
        verify(observador).atualizar(pedido, StatusPedido.CANCELADO);
    }

    @Test
    void deveMarcarPagamentoRecusadoELancarExcecao() {
        ValidadorPedido validador = mock(ValidadorPedido.class);
        ObservadorPedido observador = mock(ObservadorPedido.class);
        EstrategiaPagamento pagamento = mock(EstrategiaPagamento.class);
        Pedido pedido = pedidoValido();
        when(pagamento.getDescricao()).thenReturn("Teste");
        when(pagamento.processar(50.0)).thenReturn(false);

        assertThrows(PagamentoRecusadoException.class,
                () -> facade(validador, observador).finalizarPedido(pedido, pagamento));

        assertEquals(StatusPedido.PAGAMENTO_RECUSADO, pedido.getStatus());
        verify(observador).atualizar(pedido, StatusPedido.PAGAMENTO_RECUSADO);
    }

    private static PedidoFacade facade(ValidadorPedido validador, ObservadorPedido observador) {
        return new PedidoFacade(ConfiguracaoSistema.getInstance(), validador, List.of(observador));
    }

    private static Pedido pedidoValido() {
        Item item = mock(Item.class);
        when(item.getPreco()).thenReturn(50.0);
        return new Pedido.Builder("PED-FACADE", "Caio")
                .comEndereco("Rua E, 50")
                .adicionarItem(item)
                .build();
    }
}
