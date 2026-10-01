package com.ecommerce.decorator;

import java.util.Objects;

public abstract class ItemDecorator implements Item {

    private final Item itemDecorado;

    protected ItemDecorator(Item itemDecorado) {
        this.itemDecorado = Objects.requireNonNull(itemDecorado, "O item decorado é obrigatório.");
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
