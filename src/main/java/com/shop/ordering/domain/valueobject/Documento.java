package com.shop.ordering.domain.valueobject;


import java.util.Objects;

public record Documento(String valor) {

    public Documento(String valor) {
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
