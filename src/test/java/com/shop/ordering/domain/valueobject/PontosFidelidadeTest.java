package com.shop.ordering.domain.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PontosFidelidadeTest {

    @Test
    void deveGerarComValor() {
        PontosFidelidade pontosFidelidade = new PontosFidelidade(10);
        Assertions.assertThat(pontosFidelidade.valor()).isEqualTo(10);
    }

    @Test
    void deveSomarValor() {
        PontosFidelidade pontosFidelidade = new PontosFidelidade(10);
        Assertions.assertThat(pontosFidelidade.somar(5).valor()).isEqualTo(15);
    }

    @Test
    void naoDeveSomarValor() {
        PontosFidelidade pontosFidelidade = new PontosFidelidade(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> pontosFidelidade.somar(-5));

        Assertions.assertThat(pontosFidelidade.valor()).isEqualTo(10);
    }

}
