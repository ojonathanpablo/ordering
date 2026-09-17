package com.shop.ordering.domain.exception;

import com.shop.ordering.domain.entity.StatusPedido;
import com.shop.ordering.domain.valueobject.id.PedidoId;

import static com.shop.ordering.domain.exception.MensagensErro.ERRO_PEDIDO_NAO_PODE_SER_ALTERADO;

public class PedidoNaoPodeSerAlteradoException extends ExcecaoDominio {

    public PedidoNaoPodeSerAlteradoException(PedidoId id, StatusPedido statusPedido) {
        super(String.format(ERRO_PEDIDO_NAO_PODE_SER_ALTERADO, id, statusPedido));
    }
}
