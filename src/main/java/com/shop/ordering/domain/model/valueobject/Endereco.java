package com.shop.ordering.domain.model.valueobject;

import com.shop.ordering.domain.model.validator.ValidacoesCampo;
import lombok.Builder;

import java.util.Objects;


public record Endereco(
        String rua,
        String numero,
        String complemento,
        String bairro,
        String cidade,
        String estado,
        CEP cep
) {
    @Builder(toBuilder = true)
    public Endereco {
        ValidacoesCampo.exigeNaoEmBranco(rua);
        ValidacoesCampo.exigeNaoEmBranco(numero);
        ValidacoesCampo.exigeNaoEmBranco(bairro);
        ValidacoesCampo.exigeNaoEmBranco(cidade);
        ValidacoesCampo.exigeNaoEmBranco(estado);
        Objects.requireNonNull(cep);

    }
}
