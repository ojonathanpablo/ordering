package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.valueobject.id.PedidoId;

import java.time.LocalDate;

import static com.shop.ordering.domain.model.exception.MensagensErro.ERRO_PEDIDO_DATA_ENTREGA_INVALIDA;

public class PedidoDataEntregaInvalidaException extends ExcecaoDominio {

    public PedidoDataEntregaInvalidaException(PedidoId id, LocalDate dataEntregaPrevista) {
        super(String.format(ERRO_PEDIDO_DATA_ENTREGA_INVALIDA, id, dataEntregaPrevista));
    }
}
