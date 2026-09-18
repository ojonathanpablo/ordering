package com.shop.ordering.domain.valueobject.id;

import com.shop.ordering.domain.utility.GeradorId;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record CarrinhoComprasId(TSID valor) {

    public CarrinhoComprasId {
        Objects.requireNonNull(valor);
    }

    public CarrinhoComprasId() {
        this(GeradorId.gerarTSID());
    }

    public CarrinhoComprasId(Long valor) {
        this(TSID.from(valor));
    }

    public CarrinhoComprasId(String valor) {
        this(TSID.from(valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }
}
