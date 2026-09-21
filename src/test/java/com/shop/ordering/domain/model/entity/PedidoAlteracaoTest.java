package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.PedidoNaoPodeSerAlteradoException;
import com.shop.ordering.domain.model.valueobject.Cobranca;
import com.shop.ordering.domain.model.valueobject.Entrega;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class PedidoAlteracaoTest {

    @Test
    public void dadoPedidoRascunho_quandoAdicionarItem_devePermitir() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        pedido.adicionaItemPedido(produto, new Quantidade(1));

        Assertions.assertThat(pedido.itensPedido()).isNotEmpty();
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarEntrega_devePermitir() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Entrega entrega = PedidoTestDataBuilder.umaEntrega();

        pedido.alterarInfoEntrega(entrega);

        Assertions.assertThat(pedido.entrega()).isEqualTo(entrega);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarCobranca_devePermitir() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Cobranca cobranca = PedidoTestDataBuilder.umaCobranca();

        pedido.alterarInfoCobranca(cobranca);

        Assertions.assertThat(pedido.cobranca()).isEqualTo(cobranca);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarMetodoPagamento_devePermitir() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        pedido.alterarMetodoPagamento(MetodoPagamento.CARTAO_CREDITO);

        Assertions.assertThat(pedido.metodoPagamento()).isEqualTo(MetodoPagamento.CARTAO_CREDITO);
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoAdicionarItem_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.adicionaItemPedido(produto, new Quantidade(1)));
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoAlterarEntrega_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        Entrega entrega = PedidoTestDataBuilder.umaEntrega();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.alterarInfoEntrega(entrega));
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoAlterarCobranca_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        Cobranca cobranca = PedidoTestDataBuilder.umaCobranca();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.alterarInfoCobranca(cobranca));
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoAlterarMetodoPagamento_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.alterarMetodoPagamento(MetodoPagamento.CARTAO_CREDITO));
    }

    @Test
    public void dadoPedidoNaoRascunho_quandoAlterarQuantidadeItem_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        ItemPedido itemPedido = pedido.itensPedido().iterator().next();

        Assertions.assertThatExceptionOfType(PedidoNaoPodeSerAlteradoException.class)
                .isThrownBy(() -> pedido.alterarQuantidadeItem(itemPedido.itemPedidoId(), new Quantidade(5)));
    }

}
