package com.shop.ordering.domain.valueobject;

import java.util.Objects;

public record PontosFidelidade(Integer valor) implements Comparable<PontosFidelidade> {

    public static final PontosFidelidade ZERO = new PontosFidelidade(0);

    public PontosFidelidade() {
        this(0);
    }

    public PontosFidelidade(Integer valor) {
        Objects.requireNonNull(valor);
        if (valor < 0) {
            throw new IllegalArgumentException();
        }
        this.valor = valor;
    }

    public PontosFidelidade somar(Integer valor){
        return somar(new PontosFidelidade(valor));
    }

    public PontosFidelidade somar(PontosFidelidade pontosFidelidade){
        Objects.requireNonNull(pontosFidelidade);

        if (pontosFidelidade.valor <= 0){
            throw new IllegalArgumentException();
        }

        return new PontosFidelidade(this.valor + pontosFidelidade.valor());
    }

    @Override
    public String toString() {
        return valor.toString();
    }

    @Override
    public int compareTo(PontosFidelidade o) {
        return this.valor.compareTo(o.valor);
    }
}
