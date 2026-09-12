package com.algashop.ordering.domain.valueobject;

import java.util.Objects;

public record CEP(String valor) {

    public CEP {
        Objects.requireNonNull(valor);
        if (valor.isBlank()) {
            throw new IllegalArgumentException();
        }
        if (valor.length() != 5) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public String toString() {
        return valor;
    }
}
