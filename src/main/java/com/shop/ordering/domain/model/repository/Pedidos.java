package com.shop.ordering.domain.model.repository;


import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;

import java.time.Year;
import java.util.List;

public interface Pedidos extends Repository<Pedido, PedidoId> {
    List<Pedido> realizadosPorClienteNoAno(ClienteId clienteId, Year ano);
    long quantidadeVendasPorClienteNoAno(ClienteId clienteId, Year ano);
    Dinheiro totalVendidoParaCliente(ClienteId clienteId);
}
