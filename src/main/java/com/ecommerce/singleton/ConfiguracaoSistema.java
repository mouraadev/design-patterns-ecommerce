package com.ecommerce.singleton;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ConfiguracaoSistema {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConfiguracaoSistema.class);
    private static final ConfiguracaoSistema INSTANCIA = new ConfiguracaoSistema();

    private final String nomeLoja;
    private final double taxaImpostoPadrao;

    private ConfiguracaoSistema() {
        this.nomeLoja = "TechShop Brasil";
        this.taxaImpostoPadrao = 0.08;
    }

    public static ConfiguracaoSistema getInstance() {
        return INSTANCIA;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public double getTaxaImpostoPadrao() {
        return taxaImpostoPadrao;
    }

    public void log(String mensagem) {
        LOGGER.info("[{}] {}", nomeLoja, mensagem);
    }
}
