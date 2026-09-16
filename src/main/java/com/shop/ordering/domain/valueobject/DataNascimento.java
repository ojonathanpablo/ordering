package com.shop.ordering.domain.valueobject;

import java.time.Duration;
import java.time.LocalDate;
import java.util.Objects;

import static com.shop.ordering.domain.exception.MensagensErro.ERRO_VALIDACAO_DATA_NASCIMENTO_DEVE_SER_PASSADA;

public record DataNascimento(LocalDate valor) {

    public DataNascimento {
        Objects.requireNonNull(valor);
        if (valor.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(ERRO_VALIDACAO_DATA_NASCIMENTO_DEVE_SER_PASSADA);
        }
    }

    public Integer idade(){
        return (int) Duration.between(valor, LocalDate.now()).toDays();
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
