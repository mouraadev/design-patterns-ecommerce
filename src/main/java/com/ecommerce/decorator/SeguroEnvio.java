package com.ecommerce.decorator;

public final class SeguroEnvio extends ItemDecorator {

    private static final double CUSTO_SEGURO = 24.90;

    public SeguroEnvio(Item itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return super.getDescricao() + " + Seguro de envio";
    }

    @Override
    public double getPreco() {
        return super.getPreco() + CUSTO_SEGURO;
    }
}
