package com.ecommerce.strategy;

/**
 * Padrão STRATEGY.
 * Define uma família de algoritmos de pagamento intercambiáveis em tempo
 * de execução, sem que o código cliente precise conhecer os detalhes de
 * cada implementação.
 */
public interface EstrategiaPagamento {
    boolean processar(double valor);
    String getDescricao();
}
