package com.ecommerce.decorator;


public abstract class ItemDecorator implements Item {

    protected final Item itemDecorado;

    protected ItemDecorator(Item itemDecorado) {
        this.itemDecorado = itemDecorado;
    }

    @Override
    public double getPreco() {
        return itemDecorado.getPreco();
    }

    @Override
    public String getDescricao() {
        return itemDecorado.getDescricao();
    }
}
