package com.shop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record InfoCobranca(NomeCompleto nomeCompleto, Documento documento, Telefone telefone, Endereco endereco) {
    public InfoCobranca {
        Objects.requireNonNull(nomeCompleto);
        Objects.requireNonNull(documento);
        Objects.requireNonNull(telefone);
        Objects.requireNonNull(endereco);
    }
}
