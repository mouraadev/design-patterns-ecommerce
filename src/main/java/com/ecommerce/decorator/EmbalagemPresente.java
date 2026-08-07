package com.ecommerce.decorator;

public class EmbalagemPresente extends ItemDecorator {

    private static final double CUSTO_EMBALAGEM = 9.90;

    public EmbalagemPresente(Item itemDecorado) {
        super(itemDecorado);
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao() + " + Embalagem para presente";
    }

    @Override
    public double getPreco() {
        return itemDecorado.getPreco() + CUSTO_EMBALAGEM;
    }
}
