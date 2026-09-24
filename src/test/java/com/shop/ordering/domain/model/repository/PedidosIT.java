package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.PedidoTestDataBuilder;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadePedido;
import com.shop.ordering.infrastruture.persistence.provider.ProvedorPersistenciaPedidos;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@Import({
        ProvedorPersistenciaPedidos.class,
        MapeadorEntidadePedido.class,
        MapeadorDominioPedido.class
})
class PedidosIT {

    private Pedidos pedidos;

    @Autowired
    public PedidosIT(Pedidos pedidos) {
        this.pedidos = pedidos;
    }

    @Test
    public void devePersistirEEncontrar() {
        Pedido pedidoOriginal = PedidoTestDataBuilder.umPedido().build();
        PedidoId pedidoId = pedidoOriginal.id();
        pedidos.adicionar(pedidoOriginal);

        Optional<Pedido> pedidoPossivel = pedidos.porId(pedidoId);

        assertThat(pedidoPossivel).isPresent();

        Pedido pedidoSalvo = pedidoPossivel.get();

        assertThat(pedidoSalvo).satisfies(
                s -> assertThat(s.id()).isEqualTo(pedidoId),
                s -> assertThat(s.clienteId()).isEqualTo(pedidoOriginal.clienteId()),
                s -> assertThat(s.valorTotal()).isEqualTo(pedidoOriginal.valorTotal()),
                s -> assertThat(s.quantidade()).isEqualTo(pedidoOriginal.quantidade()),
                s -> assertThat(s.realizadoEm()).isEqualTo(pedidoOriginal.realizadoEm()),
                s -> assertThat(s.pagoEm()).isEqualTo(pedidoOriginal.pagoEm()),
                s -> assertThat(s.canceladoEm()).isEqualTo(pedidoOriginal.canceladoEm()),
                s -> assertThat(s.prontoEm()).isEqualTo(pedidoOriginal.prontoEm()),
                s -> assertThat(s.statusPedido()).isEqualTo(pedidoOriginal.statusPedido()),
                s -> assertThat(s.metodoPagamento()).isEqualTo(pedidoOriginal.metodoPagamento())
        );
    }
}