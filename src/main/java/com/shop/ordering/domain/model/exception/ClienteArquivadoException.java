package com.shop.ordering.domain.model.exception;

import static com.shop.ordering.domain.model.exception.MensagensErro.ERRO_CLIENTE_ARQUIVADO;

public class ClienteArquivadoException extends ExcecaoDominio {

    public ClienteArquivadoException() {
        super(ERRO_CLIENTE_ARQUIVADO);
    }

    public ClienteArquivadoException(Throwable causa) {
        super(ERRO_CLIENTE_ARQUIVADO, causa);
    }
}
