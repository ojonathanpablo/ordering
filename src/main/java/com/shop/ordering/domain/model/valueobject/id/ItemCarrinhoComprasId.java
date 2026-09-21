package com.shop.ordering.domain.model.valueobject.id;

import com.shop.ordering.domain.model.utility.GeradorId;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record ItemCarrinhoComprasId(TSID valor) {

    public ItemCarrinhoComprasId {
        Objects.requireNonNull(valor);
    }

    public ItemCarrinhoComprasId() {
        this(GeradorId.gerarTSID());
    }

    public ItemCarrinhoComprasId(Long valor) {
        this(TSID.from(valor));
    }

    public ItemCarrinhoComprasId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
