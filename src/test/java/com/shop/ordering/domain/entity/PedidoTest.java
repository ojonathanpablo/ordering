package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.PedidoDataEntregaInvalidaException;
import com.shop.ordering.domain.exception.StatusPedidoNaoPodeSerAlterado;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.PedidoId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;
import org.apache.commons.validator.routines.DomainValidator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Set;

class PedidoTest {

    @Test
    public void deveGerar() {
        Pedido pedido = Pedido.rascunho(new ClienteId());
    }

    @Test
    public void deveAdicionaItem() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        pedido.adicionaItemPedido(
                new ProdutoId(),
                new NomeProduto("Mause pad"),
                new Dinheiro("100"),
                new Quantidade(1)

        );

        Assertions.assertThat(pedido.itensPedido().size()).isEqualTo(1);

        ItemPedido itemPedido = pedido.itensPedido().iterator().next();

        Assertions.assertWith(itemPedido,
                (i) -> Assertions.assertThat(i.itemPedidoId()).isNotNull(),
                (i) -> Assertions.assertThat(i.nomeProduto()).isEqualTo(new NomeProduto("Mause pad")),
                (i) -> Assertions.assertThat(i.preco()).isEqualTo(new Dinheiro("100")),
                (i) -> Assertions.assertThat(i.quantidade()).isEqualTo(new Quantidade(1)),
                (i) -> Assertions.assertThat(i.nomeProduto()).isEqualTo(new NomeProduto("Mause pad"))

        );
    }

    @Test
    public void DeveLancarExcecaoTentarAlterarConjuntoItens() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        pedido.adicionaItemPedido(
                new ProdutoId(),
                new NomeProduto("Mause pad"),
                new Dinheiro("100"),
                new Quantidade(1)

        );

        Set<ItemPedido> itens = pedido.itensPedido();

        Assertions.assertThatExceptionOfType(UnsupportedOperationException.class)
                .isThrownBy(itens::clear);


    }

    @Test
    public void DeveCalcularTotais() {
        Pedido pedido = Pedido.rascunho(new ClienteId());

        pedido.adicionaItemPedido(
                new ProdutoId(),
                new NomeProduto("Mause pad"),
                new Dinheiro("100"),
                new Quantidade(2)

        );

        pedido.adicionaItemPedido(
                new ProdutoId(),
                new NomeProduto("Memoria RAM"),
                new Dinheiro("50"),
                new Quantidade(1)

        );

        Assertions.assertThat(pedido.valorTotal()).isEqualTo(new Dinheiro("250"));
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
    public void dadoPedidoRascunho_quandoAlterarInfoCobranca_devePermitirAlteracao() {
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

        InfoCobranca infoCobranca = InfoCobranca.builder()
                .endereco(endereco)
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .build();

        pedido.alterarInfoCobranca(infoCobranca);

        InfoCobranca infoCobrancaEsperada = InfoCobranca.builder()
                .endereco(endereco)
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .build();

        Assertions.assertThat(pedido.infoCobranca()).isEqualTo(infoCobrancaEsperada);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarInfoEntrega_devePermitirAlteracao() {
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

        InfoEntrega infoEntrega = InfoEntrega.builder()
                .endereco(endereco)
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .build();

        Dinheiro custoEntrega = new Dinheiro("10");
        LocalDate dataEntregaPrevista = LocalDate.now().plusDays(2);

        pedido.alterarInfoEntrega(infoEntrega, custoEntrega, dataEntregaPrevista);

        Assertions.assertThat(pedido.infoEntrega()).isEqualTo(infoEntrega);
        Assertions.assertThat(pedido.custoEntrega()).isEqualTo(custoEntrega);
        Assertions.assertThat(pedido.dataEntregaPrevista()).isEqualTo(dataEntregaPrevista);
    }

    @Test
    public void dadoPedidoRascunho_quandoAlterarInfoEntregaComDataPassada_deveGerarExcecao() {
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

        InfoEntrega infoEntrega = InfoEntrega.builder()
                .endereco(endereco)
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .build();

        Dinheiro custoEntrega = new Dinheiro("10");
        LocalDate dataEntregaPassada = LocalDate.now().minusDays(2);

        Assertions.assertThatExceptionOfType(PedidoDataEntregaInvalidaException.class)
                .isThrownBy(() -> pedido.alterarInfoEntrega(infoEntrega, custoEntrega, dataEntregaPassada));
    }

}

