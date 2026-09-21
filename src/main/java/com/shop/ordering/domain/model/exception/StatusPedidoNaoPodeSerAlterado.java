package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;

import static com.shop.ordering.domain.model.exception.MensagensErro.ERRO_STATUS_PEDIDO_NAO_PODE_ALTERAR;

public class StatusPedidoNaoPodeSerAlterado extends ExcecaoDominio {


    public StatusPedidoNaoPodeSerAlterado(PedidoId id, StatusPedido statusPedido, StatusPedido newStatus) {
        super(String.format(ERRO_STATUS_PEDIDO_NAO_PODE_ALTERAR, id, statusPedido, newStatus));
    }
}
