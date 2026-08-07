package com.ecommerce.decorator;

public class SeguroEnvio extends ItemDecorator {

    private static final double CUSTO_SEGURO = 24.90;

    public SeguroEnvio(Item itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Seguro de envio";
    }

    @Override
    public double getPreco() {
        return itemDecorado.getPreco() + CUSTO_SEGURO;
    }
}
