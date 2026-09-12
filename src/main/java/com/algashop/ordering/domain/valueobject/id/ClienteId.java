package com.algashop.ordering.domain.valueobject.id;

import com.algashop.ordering.domain.utility.GeradorId;

import java.util.Objects;
import java.util.UUID;

public record ClienteId(UUID valor) {

    public ClienteId(){
        this(GeradorId.gerarUUIDBaseadoTempo());
    }

    public ClienteId(UUID valor){
        Objects.requireNonNull(valor);
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
