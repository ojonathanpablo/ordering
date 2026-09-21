package com.shop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Dinheiro(BigDecimal valor) implements Comparable<Dinheiro> {

    private static final RoundingMode modoArredondamento = RoundingMode.HALF_EVEN;

    public static final Dinheiro ZERO = new Dinheiro(BigDecimal.ZERO);

    public Dinheiro(String valor) {
        this(new BigDecimal(valor));
    }

    public Dinheiro(BigDecimal valor) {
        Objects.requireNonNull(valor); //todo mensagem
        this.valor = valor.setScale(2, modoArredondamento);
        if (this.valor.signum() == -1) {
            throw new IllegalArgumentException();//todo mensagem
        }
    }

    public Dinheiro multiplicar(Quantidade quantidade) {
        Objects.requireNonNull(quantidade);
        if (quantidade.valor() < 1) {
            throw new IllegalArgumentException();
        }
        BigDecimal multiplicado = this.valor.multiply(new BigDecimal(quantidade.valor()));
        return new Dinheiro(multiplicado);
    }

    public Dinheiro somar(Dinheiro dinheiro) {
        Objects.requireNonNull(dinheiro);
        return new Dinheiro(this.valor.add(dinheiro.valor));
    }

    @Override
    public String toString() {
        return valor.toString();
    }

    @Override
    public int compareTo(Dinheiro o) {
        return this.valor.compareTo(o.valor);
    }

    public Dinheiro dividir(Dinheiro o) {
        return new Dinheiro(this.valor.divide(o.valor, modoArredondamento));
    }
}
