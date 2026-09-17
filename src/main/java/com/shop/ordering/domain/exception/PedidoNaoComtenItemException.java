package com.shop.ordering.domain.exception;

import com.shop.ordering.domain.valueobject.id.ItemPedidoId;
import com.shop.ordering.domain.valueobject.id.PedidoId;

public class PedidoNaoComtenItemException extends ExcecaoDominio {

    public PedidoNaoComtenItemException(PedidoId id, ItemPedidoId itemPedidoId) {
        super(String.format(MensagensErro.ERRO_PEDIDO_NAO_CONTEM_ITEM, id, itemPedidoId));
    }
}
