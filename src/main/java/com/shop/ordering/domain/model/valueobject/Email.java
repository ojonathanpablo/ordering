package com.shop.ordering.domain.model.valueobject;

import static com.shop.ordering.domain.model.validator.ValidacoesCampo.exigeEmailValido;

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
