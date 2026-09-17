package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.PedidoNaoComtenItemException;
import com.shop.ordering.domain.exception.PedidoNaoPodeSerAlteradoException;
import com.shop.ordering.domain.valueobject.Dinheiro;
import com.shop.ordering.domain.valueobject.Produto;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.ItemPedidoId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoRemoverItemTest {

    @Test
    public void dadoPedidoRascunhoComDoisItens_quandoRemoverItem_deveAtualizarTotais() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Produto memoria = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();
        pedido.adicionaItemPedido(memoria, new Quantidade(2));

        Produto notebook = ProdutoTestDataBuilder.umProduto().build();
        pedido.adicionaItemPedido(notebook, new Quantidade(1));

        ItemPedido itemRemovido = pedido.itensPedido().stream()
                .filter(i -> i.nomeProduto().equals(memoria.nomeProduto()))
                .findFirst()
                .orElseThrow();

        pedido.removeItemPedido(itemRemovido.itemPedidoId());

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.itensPedido()).hasSize(1),
                p -> Assertions.assertThat(p.itensPedido()).noneMatch(i -> i.itemPedidoId().equals(itemRemovido.itemPedidoId())),
                p -> Assertions.assertThat(p.valorTotal()).isEqualTo(new Dinheiro("3000")),
                p -> Assertions.assertThat(p.quantidade()).isEqualTo(new Quantidade(1))
        );
    }

    @Test
    public void dadoPedidoRascunho_quandoRemoverItemInexistente_deveGerarExcecao() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Produto produto = ProdutoTestDataBuilder.umProduto().build();
        pedido.adicionaItemPedido(produto, new Quantidade(1));

        ItemPedidoId itemPedidoIdInexistente = new ItemPedidoId();

        Assertions.assertThatExceptionOfType(PedidoNaoComtenItemException.class)
                .isThrownBy(() -> pedido.removeItemPedido(itemPedidoIdInexistente));
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoRemoverItem_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        ItemPedido itemPedido = pedido.itensPedido().iterator().next();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.removeItemPedido(itemPedido.itemPedidoId()));
    }

}
