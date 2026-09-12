package com.algashop.ordering.domain.exception;

import static com.algashop.ordering.domain.exception.MensagensErro.ERRO_CLIENTE_ARQUIVADO;

public class ClienteArquivadoException extends ExcecaoDominio {

    public ClienteArquivadoException() {
        super(ERRO_CLIENTE_ARQUIVADO);
    }

    public ClienteArquivadoException(Throwable causa) {
        super(ERRO_CLIENTE_ARQUIVADO, causa);
    }
}
