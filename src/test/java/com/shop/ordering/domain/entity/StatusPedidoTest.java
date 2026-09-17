package com.shop.ordering.domain.entity;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class StatusPedidoTest {

    @Test
    public void podeMudarPara() {
        Assertions.assertThat(StatusPedido.RASCUNHO.podeMudarPara(StatusPedido.REALIZADO)).isTrue();
        Assertions.assertThat(StatusPedido.RASCUNHO.podeMudarPara(StatusPedido.CANCELADO)).isTrue();
        Assertions.assertThat(StatusPedido.PAGO.podeMudarPara(StatusPedido.RASCUNHO)).isFalse();
    }

    @Test
    public void naoPodeMudarPara() {
        Assertions.assertThat(StatusPedido.REALIZADO.naoPodeMudarPara(StatusPedido.RASCUNHO)).isTrue();
    }

}
