package com.ecommerce.strategy;

public class PagamentoBoleto implements EstrategiaPagamento {

    @Override
    public boolean processar(double valor) {
        System.out.printf("Gerando boleto no valor de R$ %.2f (vencimento em 3 dias úteis)%n", valor);
        return true;
    }

    @Override
    public String getDescricao() {
        return "Boleto Bancário";
    }
}
