package com.ecommerce.strategy;

public class PagamentoCartaoCredito implements EstrategiaPagamento {

    private final String numeroCartao;
    private final int parcelas;

    public PagamentoCartaoCredito(String numeroCartao, int parcelas) {
        this.numeroCartao = numeroCartao;
        this.parcelas = parcelas;
    }

    @Override
    public boolean processar(double valor) {
        String final4 = numeroCartao.length() >= 4
                ? numeroCartao.substring(numeroCartao.length() - 4)
                : numeroCartao;
        System.out.printf("Cobrando R$ %.2f no cartão final %s em %dx%n", valor, final4, parcelas);
        return true;
    }

    @Override
    public String getDescricao() {
        return "Cartão de Crédito (" + parcelas + "x)";
    }
}
