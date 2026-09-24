package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.MetodoPagamento;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

class MapeadorDominioPedidoTest {

    private final MapeadorDominioPedido mapeadorDominioPedido = new MapeadorDominioPedido();

    @Test
    public void deveConverterDaPersistenciaParaDominio() {
        EntidadePersistenciaPedido entidadePersistenciaPedido = EntidadePersistenciaPedido.builder()
                .id(1L)
                .clienteId(UUID.randomUUID())
                .valorTotal(new BigDecimal("100.00"))
                .quantidade(2)
                .status("REALIZADO")
                .metodoPagamento("CARTAO_CREDITO")
                .realizadoEm(OffsetDateTime.now())
                .pagoEm(OffsetDateTime.now())
                .canceladoEm(null)
                .prontoEm(null)
                .build();

        Pedido pedido = mapeadorDominioPedido.paraDominio(entidadePersistenciaPedido);

        Assertions.assertThat(pedido).satisfies(
                s -> Assertions.assertThat(s.id()).isEqualTo(new PedidoId(entidadePersistenciaPedido.getId())),
                s -> Assertions.assertThat(s.clienteId()).isEqualTo(new ClienteId(entidadePersistenciaPedido.getClienteId())),
                s -> Assertions.assertThat(s.valorTotal()).isEqualTo(new Dinheiro(entidadePersistenciaPedido.getValorTotal())),
                s -> Assertions.assertThat(s.quantidade()).isEqualTo(new Quantidade(entidadePersistenciaPedido.getQuantidade())),
                s -> Assertions.assertThat(s.realizadoEm()).isEqualTo(entidadePersistenciaPedido.getRealizadoEm()),
                s -> Assertions.assertThat(s.pagoEm()).isEqualTo(entidadePersistenciaPedido.getPagoEm()),
                s -> Assertions.assertThat(s.canceladoEm()).isEqualTo(entidadePersistenciaPedido.getCanceladoEm()),
                s -> Assertions.assertThat(s.prontoEm()).isEqualTo(entidadePersistenciaPedido.getProntoEm()),
                s -> Assertions.assertThat(s.statusPedido()).isEqualTo(StatusPedido.valueOf(entidadePersistenciaPedido.getStatus())),
                s -> Assertions.assertThat(s.metodoPagamento()).isEqualTo(MetodoPagamento.valueOf(entidadePersistenciaPedido.getMetodoPagamento()))
        );
    }

}
