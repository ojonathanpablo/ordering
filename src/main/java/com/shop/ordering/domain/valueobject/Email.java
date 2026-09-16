package com.shop.ordering.domain.valueobject;

import static com.shop.ordering.domain.validator.ValidacoesCampo.exigeEmailValido;

public record Email(String valor) {

    public Email(String valor){
        exigeEmailValido(valor);
        this.valor = valor;
    }

    @Override
    public String toString() {
        return valor;
    }
}
