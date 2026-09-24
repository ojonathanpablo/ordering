package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.MetodoPagamento;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.stereotype.Component;

import java.util.HashSet;

@Component
public class MapeadorDominioPedido {

    public Pedido paraDominio(EntidadePersistenciaPedido entidadePersistenciaPedido) {
        return Pedido.builder()
                .id(new PedidoId(entidadePersistenciaPedido.getId()))
                .clienteId(new ClienteId(entidadePersistenciaPedido.getClienteId()))
                .valorTotal(new Dinheiro(entidadePersistenciaPedido.getValorTotal()))
                .totalItens(new Quantidade(entidadePersistenciaPedido.getQuantidade()))
                .status(StatusPedido.valueOf(entidadePersistenciaPedido.getStatus()))
                .metodoPagamento(MetodoPagamento.valueOf(entidadePersistenciaPedido.getMetodoPagamento()))
                .realizadoEm(entidadePersistenciaPedido.getRealizadoEm())
                .pagoEm(entidadePersistenciaPedido.getPagoEm())
                .canceladoEm(entidadePersistenciaPedido.getCanceladoEm())
                .prontoEm(entidadePersistenciaPedido.getProntoEm())
                .itens(new HashSet<>())
                .versao(entidadePersistenciaPedido.getVersion())
                .existente();
    }

}
