package com.shop.ordering.domain.valueobject;

import com.shop.ordering.domain.validator.ValidacoesCampo;

public record NomeProduto(String valor) {

    public NomeProduto {
        ValidacoesCampo.exigeNaoEmBranco(valor);
    }

    @Override
    public String toString() {
        return valor;
    }
}
