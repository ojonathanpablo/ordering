package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.stereotype.Component;

@Component
public class MapeadorEntidadePedido {

    public EntidadePersistenciaPedido paraEntidade(Pedido pedido) {
        return mesclar(new EntidadePersistenciaPedido(), pedido);
    }

    public EntidadePersistenciaPedido mesclar(EntidadePersistenciaPedido entidadePersistenciaPedido, Pedido pedido) {
        entidadePersistenciaPedido.setId(pedido.id().valor().toLong());
        entidadePersistenciaPedido.setClienteId(pedido.clienteId().valor());
        entidadePersistenciaPedido.setValorTotal(pedido.valorTotal().valor());
        entidadePersistenciaPedido.setQuantidade(pedido.quantidade().valor());
        entidadePersistenciaPedido.setStatus(pedido.statusPedido().name());
        entidadePersistenciaPedido.setMetodoPagamento(pedido.metodoPagamento().name());
        entidadePersistenciaPedido.setRealizadoEm(pedido.realizadoEm());
        entidadePersistenciaPedido.setPagoEm(pedido.pagoEm());
        entidadePersistenciaPedido.setCanceladoEm(pedido.canceladoEm());
        entidadePersistenciaPedido.setProntoEm(pedido.prontoEm());
        entidadePersistenciaPedido.setVersion(pedido.versao());
        return entidadePersistenciaPedido;
    }

}
