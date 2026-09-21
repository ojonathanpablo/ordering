package com.shop.ordering.domain.model.validator;


import org.apache.commons.validator.routines.EmailValidator;

import java.util.Objects;

public class ValidacoesCampo {

    private ValidacoesCampo() {
    }

    public static void exigeEmailValido(String email) {
        exigeEmailValido(email, null);
    }

    public static void exigeEmailValido(String email, String mensagemErro) {
        Objects.requireNonNull(email, mensagemErro);
        if (email.isBlank()) {
            throw new IllegalArgumentException(mensagemErro);
        }
        if (!EmailValidator.getInstance().isValid(email)) {
            throw new IllegalArgumentException(mensagemErro);
        }
    }

    public static void exigeNaoEmBranco(String valor) {
        exigeNaoEmBranco(valor, " ");
    }

    public static void exigeNaoEmBranco(String valor, String mensagemErro) {
        Objects.requireNonNull(valor);
        if (valor.isBlank()) {
            throw new IllegalArgumentException();
        }
    }
}
