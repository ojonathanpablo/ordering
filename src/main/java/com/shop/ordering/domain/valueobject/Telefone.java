package com.shop.ordering.domain.valueobject;

import java.util.Objects;

public record Telefone(String valor) {

    public Telefone(String valor) {
        Objects.requireNonNull(valor);
        if (valor.isBlank()) {
            throw new IllegalArgumentException();
        }
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
