package com.shop.ordering.domain.valueobject.id;

import com.shop.ordering.domain.utility.GeradorId;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record PedidoId(TSID valor) {

    public PedidoId {
        Objects.requireNonNull(valor);
    }

    public PedidoId() {
        this(GeradorId.gerarTSID());
    }

    public PedidoId(Long valor) {
        this(TSID.from(valor));
    }

    public PedidoId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
