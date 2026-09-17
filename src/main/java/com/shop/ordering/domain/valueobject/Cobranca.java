package com.shop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder(toBuilder = true)
public record Cobranca(Recebedor recebedor, Endereco endereco) {

    public Cobranca {
        Objects.requireNonNull(recebedor);
        Objects.requireNonNull(endereco);
    }
}
