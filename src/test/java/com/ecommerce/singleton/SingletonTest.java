package com.ecommerce.singleton;

import org.junit.jupiter.api.Test;

import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class SingletonTest {

    @Test
    void deveRetornarSempreAMesmaInstanciaInclusiveEmParalelo() {
        ConfiguracaoSistema esperada = ConfiguracaoSistema.getInstance();

        IntStream.range(0, 100).parallel()
                .forEach(indice -> assertSame(esperada, ConfiguracaoSistema.getInstance()));

        assertEquals("TechShop Brasil", esperada.getNomeLoja());
        assertEquals(0.08, esperada.getTaxaImpostoPadrao(), 0.001);
    }
}
