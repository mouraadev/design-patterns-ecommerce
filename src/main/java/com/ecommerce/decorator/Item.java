package com.ecommerce.decorator;

/**
 * Componente comum do padrão DECORATOR: tanto o produto "puro" quanto suas
 * versões decoradas (com embalagem, seguro, etc.) implementam esta interface.
 */
public interface Item {
    String getDescricao();
    double getPreco();
}
