package com.ecommerce.strategy;

public interface EstrategiaPagamento {

    boolean processar(double valor);

    String getDescricao();
}
