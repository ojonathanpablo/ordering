package com.shop.ordering.domain.model.valueobject.id;

import com.shop.ordering.domain.model.utility.GeradorId;

import java.util.Objects;
import java.util.UUID;

public record ClienteId(UUID valor) {

    public ClienteId(){
        this(GeradorId.gerarUUIDBaseadoTempo());
    }

    public ClienteId {
        Objects.requireNonNull(valor);
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
