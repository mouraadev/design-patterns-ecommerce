package com.ecommerce.facade;

import com.ecommerce.builder.Pedido;
import com.ecommerce.chain.ValidadorEndereco;
import com.ecommerce.chain.ValidadorEstoque;
import com.ecommerce.chain.ValidadorFraude;
import com.ecommerce.chain.ValidadorPedido;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.observer.NotificadorEmail;
import com.ecommerce.observer.NotificadorSms;
import com.ecommerce.singleton.ConfiguracaoSistema;
import com.ecommerce.strategy.EstrategiaPagamento;

/**
 * Padrão FACADE.
 * Oferece uma interface simples ("finalizarPedido") que esconde a
 * complexidade de coordenar validação (Chain of Responsibility), pagamento
 * (Strategy), notificações (Observer) e configuração (Singleton).
 */
public class PedidoFacade {

    private final ConfiguracaoSistema config = ConfiguracaoSistema.getInstance();

    public void finalizarPedido(Pedido pedido, EstrategiaPagamento estrategiaPagamento) {
        pedido.adicionarObservador(new NotificadorEmail());
        pedido.adicionarObservador(new NotificadorSms());

        config.log("Iniciando processamento do pedido " + pedido.getId());

        ValidadorPedido validadorEstoque = new ValidadorEstoque();
        ValidadorPedido validadorEndereco = new ValidadorEndereco();
        ValidadorPedido validadorFraude = new ValidadorFraude();
        validadorEstoque.definirProximo(validadorEndereco).definirProximo(validadorFraude);

        if (!validadorEstoque.validar(pedido)) {
            pedido.atualizarStatus(StatusPedido.CANCELADO);
            config.log("Pedido " + pedido.getId() + " cancelado na validação.");
            return;
        }

        config.log("Processando pagamento via " + estrategiaPagamento.getDescricao());
        boolean pagamentoOk = estrategiaPagamento.processar(pedido.getValorTotal());

        if (!pagamentoOk) {
            pedido.atualizarStatus(StatusPedido.PAGAMENTO_RECUSADO);
            return;
        }

        pedido.atualizarStatus(StatusPedido.PAGAMENTO_APROVADO);
        pedido.atualizarStatus(StatusPedido.EM_SEPARACAO);
        pedido.atualizarStatus(StatusPedido.ENVIADO);

        config.log("Pedido " + pedido.getId() + " finalizado com sucesso!");
    }
}
