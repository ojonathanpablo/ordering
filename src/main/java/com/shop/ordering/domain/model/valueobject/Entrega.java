package com.shop.ordering.domain.model.valueobject;

import lombok.Builder;

import java.time.LocalDate;
import java.util.Objects;

@Builder
public record Entrega(Dinheiro custo, LocalDate dataPrevista, Recebedor recebedor, Endereco endereco) {

    public Entrega {
        Objects.requireNonNull(custo);
        Objects.requireNonNull(dataPrevista);
        Objects.requireNonNull(recebedor);
        Objects.requireNonNull(endereco);
    }
}
