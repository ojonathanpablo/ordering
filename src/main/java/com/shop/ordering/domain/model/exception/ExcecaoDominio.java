package com.shop.ordering.domain.model.exception;

public class ExcecaoDominio extends RuntimeException {

    public ExcecaoDominio(String mensagem, Throwable causa) {
        super(mensagem, causa);
    }

    public ExcecaoDominio(String mensagem) {
        super(mensagem);
    }
}
