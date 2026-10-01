package com.ecommerce.facade;

import com.ecommerce.builder.Pedido;
import com.ecommerce.chain.ValidadorEndereco;
import com.ecommerce.chain.ValidadorEstoque;
import com.ecommerce.chain.ValidadorFraude;
import com.ecommerce.chain.ValidadorPedido;
import com.ecommerce.enums.StatusPedido;
import com.ecommerce.exception.PagamentoRecusadoException;
import com.ecommerce.exception.PedidoInvalidoException;
import com.ecommerce.observer.NotificadorEmail;
import com.ecommerce.observer.NotificadorSms;
import com.ecommerce.observer.ObservadorPedido;
import com.ecommerce.singleton.ConfiguracaoSistema;
import com.ecommerce.strategy.EstrategiaPagamento;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public final class PedidoFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(PedidoFacade.class);

    private final ConfiguracaoSistema config;
    private final ValidadorPedido validador;
    private final List<ObservadorPedido> observadores;

    public PedidoFacade() {
        this(ConfiguracaoSistema.getInstance(), criarCadeiaPadrao(),
                List.of(new NotificadorEmail(), new NotificadorSms()));
    }

    public PedidoFacade(ConfiguracaoSistema config, ValidadorPedido validador,
                        List<ObservadorPedido> observadores) {
        this.config = Objects.requireNonNull(config, "A configuração é obrigatória.");
        this.validador = Objects.requireNonNull(validador, "O validador é obrigatório.");
        this.observadores = List.copyOf(
                Objects.requireNonNull(observadores, "A lista de observadores é obrigatória."));
    }

    public void finalizarPedido(Pedido pedido, EstrategiaPagamento estrategiaPagamento) {
        Objects.requireNonNull(pedido, "O pedido é obrigatório.");
        Objects.requireNonNull(estrategiaPagamento, "A estratégia de pagamento é obrigatória.");
        if (pedido.getStatus() != StatusPedido.CRIADO) {
            throw new PedidoInvalidoException("Somente pedidos com status CRIADO podem ser finalizados.");
        }

        observadores.forEach(pedido::adicionarObservador);

        LOGGER.info("[{}] Iniciando processamento do pedido {}", config.getNomeLoja(), pedido.getId());

        try {
            validador.validar(pedido);
        } catch (PedidoInvalidoException excecao) {
            pedido.atualizarStatus(StatusPedido.CANCELADO);
            LOGGER.warn("[{}] Pedido {} cancelado na validação: {}",
                    config.getNomeLoja(), pedido.getId(), excecao.getMessage());
            throw excecao;
        }

        LOGGER.info("[{}] Processando pagamento via {}", config.getNomeLoja(),
                estrategiaPagamento.getDescricao());
        boolean pagamentoOk = estrategiaPagamento.processar(pedido.getValorTotal());

        if (!pagamentoOk) {
            pedido.atualizarStatus(StatusPedido.PAGAMENTO_RECUSADO);
            throw new PagamentoRecusadoException("Pagamento recusado para o pedido " + pedido.getId() + ".");
        }

        pedido.atualizarStatus(StatusPedido.PAGAMENTO_APROVADO);
        pedido.atualizarStatus(StatusPedido.EM_SEPARACAO);
        pedido.atualizarStatus(StatusPedido.ENVIADO);

        LOGGER.info("[{}] Pedido {} finalizado com sucesso", config.getNomeLoja(), pedido.getId());
    }

    private static ValidadorPedido criarCadeiaPadrao() {
        ValidadorPedido estoque = new ValidadorEstoque();
        estoque.definirProximo(new ValidadorEndereco())
                .definirProximo(new ValidadorFraude());
        return estoque;
    }
}
