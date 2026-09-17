package com.shop.ordering.domain.exception;

import com.shop.ordering.domain.valueobject.id.PedidoId;

import static com.shop.ordering.domain.exception.MensagensErro.ERRO_PEDIDO_NAO_PODE_SER_REALIZADO;

public class PedidoNaoPodeSerRealizadoException extends ExcecaoDominio {

    public PedidoNaoPodeSerRealizadoException(PedidoId id) {
        super(String.format(ERRO_PEDIDO_NAO_PODE_SER_REALIZADO, id));
    }
}
