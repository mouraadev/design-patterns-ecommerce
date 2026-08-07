package com.ecommerce.singleton;

/**
 * Padrão SINGLETON.
 * Garante que exista uma única instância de configuração/log do sistema,
 * acessível globalmente através de {@link #getInstance()}.
 */
public final class ConfiguracaoSistema {

    private static ConfiguracaoSistema instancia;

    private final String nomeLoja;
    private final double taxaImpostoPadrao;

    private ConfiguracaoSistema() {
        this.nomeLoja = "TechShop Brasil";
        this.taxaImpostoPadrao = 0.08;
    }

    public static synchronized ConfiguracaoSistema getInstance() {
        if (instancia == null) {
            instancia = new ConfiguracaoSistema();
        }
        return instancia;
    }

    public String getNomeLoja() {
        return nomeLoja;
    }

    public double getTaxaImpostoPadrao() {
        return taxaImpostoPadrao;
    }

    public void log(String mensagem) {
        System.out.println("[" + nomeLoja + "] " + mensagem);
    }
}
