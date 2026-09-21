package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemPedidoTest {

    @Test
    public void deveGerarItemPedidoNovo() {
        Produto produto = ProdutoTestDataBuilder.umProduto().build();
        Quantidade quantidade = new Quantidade(1);
        PedidoId pedidoId = new PedidoId();

        ItemPedido itemPedido = ItemPedido.novo()
                .produto(produto)
                .quantidade(quantidade)
                .pedidoId(pedidoId)
                .build();

        Assertions.assertWith(itemPedido,
                i -> Assertions.assertThat(i.itemPedidoId()).isNotNull(),
                i -> Assertions.assertThat(i.produtoId()).isEqualTo(produto.produtoId()),
                i -> Assertions.assertThat(i.nomeProduto()).isEqualTo(produto.nomeProduto()),
                i -> Assertions.assertThat(i.preco()).isEqualTo(produto.preco()),
                i -> Assertions.assertThat(i.quantidade()).isEqualTo(quantidade),
                i -> Assertions.assertThat(i.pedidoId()).isEqualTo(pedidoId)
        );
    }
}
