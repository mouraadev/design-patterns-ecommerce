package com.ecommerce.chain;

import com.ecommerce.builder.Pedido;

public class ValidadorFraude extends ValidadorPedido {

    private static final double VALOR_SUSPEITO = 10_000.0;

    @Override
    protected boolean executarValidacao(Pedido pedido) {
        boolean seguro = pedido.getValorTotal() < VALOR_SUSPEITO;
        System.out.println("[Validação] Análise antifraude... " + (seguro ? "OK" : "PEDIDO SUSPEITO"));
        return seguro;
    }
}
