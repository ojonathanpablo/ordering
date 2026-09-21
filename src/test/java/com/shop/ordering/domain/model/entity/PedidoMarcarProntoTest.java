package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.StatusPedidoNaoPodeSerAlterado;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoMarcarProntoTest {

    @Test
    public void dadoPedidoPago_quandoMarcarPronto_deveMudarParaPronto() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.PAGO).build();

        pedido.marcaPronto();

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isPronto()).isTrue(),
                p -> Assertions.assertThat(p.prontoEm()).isNotNull()
        );
    }

    @Test
    public void dadoPedidoNaoPago_quandoMarcarPronto_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();

        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeSerAlterado.class)
                .isThrownBy(pedido::marcaPronto);

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.isPronto()).isFalse(),
                p -> Assertions.assertThat(p.prontoEm()).isNull()
        );
    }

}
