package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.StatusPedidoNaoPodeSerAlterado;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoCancelarTest {

    @Test
    public void dadoPedidoRascunho_quandoCancelar_deveMudarParaCancelado() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.RASCUNHO).build();

        pedido.cancelar();

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isCancelado()).isTrue(),
                p -> Assertions.assertThat(p.canceladoEm()).isNotNull()
        );
    }

    @Test
    public void dadoPedidoRealizado_quandoCancelar_deveMudarParaCancelado() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();

        pedido.cancelar();

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isCancelado()).isTrue(),
                p -> Assertions.assertThat(p.canceladoEm()).isNotNull()
        );
    }

    @Test
    public void dadoPedidoPago_quandoCancelar_deveMudarParaCancelado() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.PAGO).build();

        pedido.cancelar();

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isCancelado()).isTrue(),
                p -> Assertions.assertThat(p.canceladoEm()).isNotNull()
        );
    }

    @Test
    public void dadoPedidoPronto_quandoCancelar_deveMudarParaCancelado() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.PRONTO).build();

        pedido.cancelar();

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isCancelado()).isTrue(),
                p -> Assertions.assertThat(p.canceladoEm()).isNotNull()
        );
    }

    @Test
    public void dadoPedidoCancelado_quandoCancelarNovamente_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.CANCELADO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeSerAlterado.class)
                .isThrownBy(pedido::cancelar);

        Assertions.assertThat(pedido.isCancelado()).isTrue();
    }

}
