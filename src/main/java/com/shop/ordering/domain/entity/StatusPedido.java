package com.shop.ordering.domain.entity;

import java.util.Arrays;
import java.util.List;

public enum StatusPedido {
    RASCUNHO,
    REALIZADO(RASCUNHO),
    PAGO(REALIZADO),
    PRONTO(PAGO),
    CANCELADO(RASCUNHO, REALIZADO, PAGO, PRONTO);

    private final List<StatusPedido> statusArteriores;

    StatusPedido(StatusPedido... statusAnteriores) {
        this.statusArteriores = Arrays.asList(statusAnteriores);
    }

    public boolean podeMudarPara(StatusPedido newStatusPedido) {
        StatusPedido statusAtual = this;
        return newStatusPedido.statusArteriores.contains(statusAtual);
    }

    public boolean naoPodeMudarPara(StatusPedido newStatus) {
        return !podeMudarPara(newStatus);
    }

}
