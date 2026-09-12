package com.algashop.ordering.domain.valueobject.id;

import com.algashop.ordering.domain.utility.GeradorId;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ItemPedidoId(TSID valor) {

    public ItemPedidoId {
        Objects.requireNonNull(valor);
    }

    public ItemPedidoId() {
        this(GeradorId.gerarTSID());
    }

    public ItemPedidoId(Long valor) {
        this(TSID.from(valor));
    }

    public ItemPedidoId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
