package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.PedidoDataEntregaInvalidaException;
import com.shop.ordering.domain.exception.ProdutoEsgotadoException;
import com.shop.ordering.domain.exception.StatusPedidoNaoPodeSerAlterado;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class PedidoTest {

    @Test
    public void deveGerarPedidoRascunho() {
        ClienteId clienteId = new ClienteId();
        Pedido pedido = Pedido.rascunho(clienteId);

        Assertions.assertWith(pedido,
                p -> Assertions.assertThat(p.id()).isNotNull(),
                p -> Assertions.assertThat(p.clienteId()).isEqualTo(clienteId),
                p -> Assertions.assertThat(p.valorTotal()).isEqualTo(Dinheiro.ZERO),
                p -> Assertions.assertThat(p.quantidade()).isEqualTo(Quantidade.ZERO),
                p -> Assertions.assertThat(p.isRascunho()).isTrue(),
                p -> Assertions.assertThat(p.itensPedido()).isEmpty(),

                p -> Assertions.assertThat(p.realizadoEm()).isNull(),
                p -> Assertions.assertThat(p.pagoEm()).isNull(),
                p -> Assertions.assertThat(p.canceladoEm()).isNull(),
                p -> Assertions.assertThat(p.prontoEm()).isNull(),
                p -> Assertions.assertThat(p.cobranca()).isNull(),
                p -> Assertions.assertThat(p.entrega()).isNull(),
                p -> Assertions.assertThat(p.metodoPagamento()).isNull()
        );
    }

    @Test
    public void deveAdicionaItem() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Produto produto = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();
        pedido.adicionaItemPedido(produto, new Quantidade(1));

        Assertions.assertThat(pedido.itensPedido().size()).isEqualTo(1);

        ItemPedido itemPedido = pedido.itensPedido().iterator().next();

        Assertions.assertWith(itemPedido,
                (i) -> Assertions.assertThat(i.itemPedidoId()).isNotNull(),
                (i) -> Assertions.assertThat(i.nomeProduto()).isEqualTo(new NomeProduto("8GB RAM")),
                (i) -> Assertions.assertThat(i.preco()).isEqualTo(new Dinheiro("300")),
                (i) -> Assertions.assertThat(i.quantidade()).isEqualTo(new Quantidade(1))

        );
    }

    @Test
    public void DeveLancarExcecaoTentarAlterarConjuntoItens() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Produto produto = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();
        pedido.adicionaItemPedido(produto, new Quantidade(1));

        Set<ItemPedido> itens = pedido.itensPedido();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(itens::clear);

    }

    @Test
    public void DeveCalcularTotais() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Produto memoria = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();
        pedido.adicionaItemPedido(memoria, new Quantidade(2));

        Produto notebook = ProdutoTestDataBuilder.umProduto().build();
        pedido.adicionaItemPedido(notebook, new Quantidade(1));

        Assertions.assertThat(pedido.valorTotal()).isEqualTo(new Dinheiro("3600"));
        Assertions.assertThat(pedido.quantidade()).isEqualTo(new Quantidade(3));

    }

    @Test
    public void dadoPedidoRascunho_quandoRealizar_deveMudarParaRealizado() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();
        pedido.realizar();
        Assertions.assertThat(pedido.isRealizado()).isTrue();
    }

    @Test
    public void dadoPedidoRealizado_quandoTentarRealizarDeNovo_deveGerarExcecao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        Assertions.assertThatExceptionOfType(StatusPedidoNaoPodeSerAlterado.class)
                .isThrownBy(pedido::realizar);
    }

    @Test
    public void dadoPedidoRealizado_quandoMarcarComoPago_deveMudarParaPago() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        pedido.marcaPago();
        Assertions.assertThat(pedido.isPago()).isTrue();
        Assertions.assertThat(pedido.pagoEm()).isNotNull();
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarMetodoPagamento_devePermitirAlteracao() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        pedido.alterarMetodoPagamento(MetodoPagamento.CARTAO_CREDITO);

        Assertions.assertThat(pedido.metodoPagamento()).isEqualTo(MetodoPagamento.CARTAO_CREDITO);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarCobranca_devePermitirAlteracao() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Endereco endereco = Endereco.builder()
                .rua("Rua Bourbon")
                .numero("1234")
                .bairro("North Ville")
                .complemento("apt. 11")
                .cidade("Montfort")
                .estado("South Carolina")
                .cep(new CEP("79911"))
                .build();

        Recebedor recebedor = Recebedor.builder()
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .build();

        Cobranca cobranca = Cobranca.builder()
                .endereco(endereco)
                .recebedor(recebedor)
                .build();

        pedido.alterarInfoCobranca(cobranca);

        Cobranca cobrancaEsperada = Cobranca.builder()
                .endereco(endereco)
                .recebedor(recebedor)
                .build();

        Assertions.assertThat(pedido.cobranca()).isEqualTo(cobrancaEsperada);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarEntrega_devePermitirAlteracao() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Endereco endereco = Endereco.builder()
                .rua("Rua Bourbon")
                .numero("1234")
                .bairro("North Ville")
                .complemento("apt. 11")
                .cidade("Montfort")
                .estado("South Carolina")
                .cep(new CEP("79911"))
                .build();

        Recebedor recebedor = Recebedor.builder()
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .build();

        Entrega entrega = Entrega.builder()
                .endereco(endereco)
                .recebedor(recebedor)
                .custo(new Dinheiro("10"))
                .dataPrevista(LocalDate.now().plusDays(2))
                .build();

        pedido.alterarInfoEntrega(entrega);

        Assertions.assertThat(pedido.entrega()).isEqualTo(entrega);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarEntregaComDataPassada_deveGerarExcecao() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Endereco endereco = Endereco.builder()
                .rua("Rua Bourbon")
                .numero("1234")
                .bairro("North Ville")
                .complemento("apt. 11")
                .cidade("Montfort")
                .estado("South Carolina")
                .cep(new CEP("79911"))
                .build();

        Recebedor recebedor = Recebedor.builder()
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .build();

        Entrega entrega = Entrega.builder()
                .endereco(endereco)
                .recebedor(recebedor)
                .custo(new Dinheiro("10"))
                .dataPrevista(LocalDate.now().minusDays(2))
                .build();

        Assertions.assertThatExceptionOfType(PedidoDataEntregaInvalidaException.class)
                .isThrownBy(() -> pedido.alterarInfoEntrega(entrega));
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarQuantidadeItem_deveRecalcular() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
        Produto produto = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();
        pedido.adicionaItemPedido(produto,new Quantidade(3));

        ItemPedido itemPedido = pedido.itensPedido().iterator().next();

        pedido.alterarQuantidadeItem(itemPedido.itemPedidoId(), new Quantidade(5));

        Assertions.assertWith(pedido,
                (p) -> Assertions.assertThat(p.valorTotal()).isEqualTo(new Dinheiro("1500")),
                (p) -> Assertions.assertThat(p.quantidade()).isEqualTo(new Quantidade(5))
        );
    }

    @Test
    public void dadoProdutoEsgotado_quandoTentarAdicionarAoPedido_naoDevePermitir() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        Assertions.assertThatExceptionOfType(ProdutoEsgotadoException.class)
                .isThrownBy(() -> pedido.adicionaItemPedido(
                        ProdutoTestDataBuilder.umProdutoIndisponivel().build(),
                        new Quantidade(1)
                ));
    }

}

