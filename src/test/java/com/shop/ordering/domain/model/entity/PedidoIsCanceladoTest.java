package com.shop.ordering.domain.model.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoIsCanceladoTest {

    @Test
    public void dadoPedidoCancelado_isCancelado_deveRetornarTrue() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.CANCELADO).build();

        Assertions.assertThat(pedido.isCancelado()).isTrue();
    }

    @Test
    public void dadoPedidoRascunho_isCancelado_deveRetornarFalse() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.RASCUNHO).build();

        Assertions.assertThat(pedido.isCancelado()).isFalse();
    }

    @Test
    public void dadoPedidoRealizado_isCancelado_deveRetornarFalse() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();

        Assertions.assertThat(pedido.isCancelado()).isFalse();
    }

    @Test
    public void dadoPedidoPago_isCancelado_deveRetornarFalse() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.PAGO).build();

        Assertions.assertThat(pedido.isCancelado()).isFalse();
    }

    @Test
    public void dadoPedidoPronto_isCancelado_deveRetornarFalse() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.PRONTO).build();

        Assertions.assertThat(pedido.isCancelado()).isFalse();
    }

}
