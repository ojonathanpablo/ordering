package com.shop.ordering.domain.model.factory;

import com.shop.ordering.domain.model.entity.MetodoPagamento;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.valueobject.Cobranca;
import com.shop.ordering.domain.model.valueobject.Entrega;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;

import java.util.Objects;

public class PedidoFactory {

    private PedidoFactory() {

    }

    public static Pedido preenchido(
            ClienteId clienteId,
            Entrega entrega,
            Cobranca cobranca,
            MetodoPagamento metodoPagamento,
            Produto produto,
            Quantidade quantidadeProduto
    ) {
        Objects.requireNonNull(clienteId);
        Objects.requireNonNull(entrega);
        Objects.requireNonNull(cobranca);
        Objects.requireNonNull(metodoPagamento);
        Objects.requireNonNull(produto);
        Objects.requireNonNull(quantidadeProduto);

        Pedido pedido = Pedido.rascunho(clienteId);

        pedido.alterarInfoCobranca(cobranca);
        pedido.alterarInfoEntrega(entrega);
        pedido.alterarMetodoPagamento(metodoPagamento);
        pedido.adicionaItemPedido(produto, quantidadeProduto);

        return pedido;
    }

}
