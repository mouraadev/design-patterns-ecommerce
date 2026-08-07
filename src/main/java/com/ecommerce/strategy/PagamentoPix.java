package com.ecommerce.strategy;

public class PagamentoPix implements EstrategiaPagamento {

    @Override
    public boolean processar(double valor) {
        System.out.printf("Gerando QR Code Pix no valor de R$ %.2f%n", valor);
        return true;
    }

    @Override
    public String getDescricao() {
        return "Pix";
    }
}
