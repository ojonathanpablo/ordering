package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.PedidoTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class MapeadorEntidadePedidoTest {

    private final MapeadorEntidadePedido mapeadorEntidadePedido = new MapeadorEntidadePedido();

    @Test
    void deveConverterDoDominioParaEntidade() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();

        EntidadePersistenciaPedido entidadePersistenciaPedido = mapeadorEntidadePedido.paraEntidade(pedido);

        Assertions.assertThat(entidadePersistenciaPedido).satisfies(
                p -> Assertions.assertThat(p.getId()).isEqualTo(pedido.id().valor().toLong()),
                p -> Assertions.assertThat(p.getClienteId()).isEqualTo(pedido.clienteId().valor()),
                p -> Assertions.assertThat(p.getValorTotal()).isEqualTo(pedido.valorTotal().valor()),
                p -> Assertions.assertThat(p.getQuantidade()).isEqualTo(pedido.quantidade().valor()),
                p -> Assertions.assertThat(p.getStatus()).isEqualTo(pedido.statusPedido().name()),
                p -> Assertions.assertThat(p.getMetodoPagamento()).isEqualTo(pedido.metodoPagamento().name()),
                p -> Assertions.assertThat(p.getRealizadoEm()).isEqualTo(pedido.realizadoEm()),
                p -> Assertions.assertThat(p.getPagoEm()).isEqualTo(pedido.pagoEm()),
                p -> Assertions.assertThat(p.getCanceladoEm()).isEqualTo(pedido.canceladoEm()),
                p -> Assertions.assertThat(p.getProntoEm()).isEqualTo(pedido.prontoEm())
        );
    }

    @Test
    void deveMesclar() {

    }

}
