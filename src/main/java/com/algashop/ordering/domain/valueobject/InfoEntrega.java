package com.algashop.ordering.domain.valueobject;

import lombok.Builder;

import java.util.Objects;

@Builder
public record InfoEntrega(NomeCompleto nomeCompleto, Documento documento, Telefone telefone, Endereco endereco) {
    public InfoEntrega {
        Objects.requireNonNull(nomeCompleto);
        Objects.requireNonNull(documento);
        Objects.requireNonNull(telefone);
        Objects.requireNonNull(endereco);
    }
}
