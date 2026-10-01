package com.ecommerce.decorator;

public final class EmbalagemPresente extends ItemDecorator {

    private static final double CUSTO_EMBALAGEM = 9.90;

    public EmbalagemPresente(Item itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return super.getDescricao() + " + Embalagem para presente";
    }

    @Override
    public double getPreco() {
        return super.getPreco() + CUSTO_EMBALAGEM;
    }
}
