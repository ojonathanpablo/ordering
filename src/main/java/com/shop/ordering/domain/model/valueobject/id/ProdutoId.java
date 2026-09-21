package com.shop.ordering.domain.model.valueobject.id;

import com.shop.ordering.domain.model.utility.GeradorId;

import java.util.Objects;
import java.util.UUID;

public record ProdutoId(UUID valor) {

    public ProdutoId(){
        this(GeradorId.gerarUUIDBaseadoTempo());
    }

    public ProdutoId(UUID valor){
        Objects.requireNonNull(valor);
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
