package com.ecommerce.factory;

public class Eletronico extends Produto {

    private final int garantiaMeses;

    public Eletronico(String nome, double preco) {
        super(nome, preco);
        this.garantiaMeses = 12;
    }

    public int getGarantiaMeses() {
        return garantiaMeses;
    }

    @Override
    public String getCategoria() {
        return "Eletrônico";
    }
}
