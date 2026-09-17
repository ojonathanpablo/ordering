package com.shop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record Recebedor(NomeCompleto nomeCompleto, Documento documento, Telefone telefone) {

    public Recebedor {
        Objects.requireNonNull(nomeCompleto);
        Objects.requireNonNull(documento);
        Objects.requireNonNull(telefone);
    }
}
