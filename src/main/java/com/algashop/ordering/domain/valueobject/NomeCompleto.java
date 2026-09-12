package com.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record NomeCompleto(String primeiroNome, String ultimoNome) {

    public NomeCompleto(String primeiroNome, String ultimoNome) {
        Objects.requireNonNull(primeiroNome);
        Objects.requireNonNull(ultimoNome);

        if (primeiroNome.isBlank()) {
            throw new IllegalArgumentException();

        }

        if (ultimoNome.isBlank()) {
            throw new IllegalArgumentException();
        }

        this.primeiroNome = primeiroNome.trim();
        this.ultimoNome = ultimoNome.trim();

    }

    @Override
    public String toString() {
        return primeiroNome + " " + ultimoNome;
    }
}
