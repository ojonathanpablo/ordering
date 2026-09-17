package com.shop.ordering.domain.factory;

import com.shop.ordering.domain.entity.MetodoPagamento;
import com.shop.ordering.domain.entity.Pedido;
import com.shop.ordering.domain.entity.PedidoTestDataBuilder;
import com.shop.ordering.domain.entity.ProdutoTestDataBuilder;
import com.shop.ordering.domain.valueobject.Cobranca;
import com.shop.ordering.domain.valueobject.Entrega;
import com.shop.ordering.domain.valueobject.Produto;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoFactoryTest {

    @Test
    public void deveGerarPedidoPreenchidoQuePodeSerRealizado() {
        Entrega entrega = PedidoTestDataBuilder.umaEntrega();
        Cobranca cobranca = PedidoTestDataBuilder.umaCobranca();

        Produto produto = ProdutoTestDataBuilder.umProduto().build();
        MetodoPagamento metodoPagamento = MetodoPagamento.SALDO_GATEWAY;

        Quantidade quantidade = new Quantidade(1);
        ClienteId clienteId = new ClienteId();

        Pedido pedido = PedidoFactory.preenchido(
                clienteId, entrega, cobranca, metodoPagamento, produto, quantidade
        );

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.entrega()).isEqualTo(entrega),
                p -> Assertions.assertThat(p.cobranca()).isEqualTo(cobranca),
                p -> Assertions.assertThat(p.metodoPagamento()).isEqualTo(metodoPagamento),
                p -> Assertions.assertThat(p.itensPedido()).isNotEmpty(),
                p -> Assertions.assertThat(p.clienteId()).isNotNull(),
                p -> Assertions.assertThat(p.isRascunho()).isTrue()
        );

        pedido.realizar();

        Assertions.assertThat(pedido.isRealizado()).isTrue();
    }

}
