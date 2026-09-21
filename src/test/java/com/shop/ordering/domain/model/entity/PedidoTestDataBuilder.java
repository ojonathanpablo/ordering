package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.valueobject.*;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;

import java.time.LocalDate;

public class PedidoTestDataBuilder {

    private ClienteId clienteId = new ClienteId();

    private MetodoPagamento metodoPagamento = MetodoPagamento.SALDO_GATEWAY;

    private Entrega entrega = umaEntrega();
    private Cobranca cobranca = umaCobranca();

    private boolean comItens = true;

    private StatusPedido status = StatusPedido.RASCUNHO;

    private PedidoTestDataBuilder() {

    }

    public static PedidoTestDataBuilder umPedido() {
        return new PedidoTestDataBuilder();
    }

    public Pedido build() {
        Pedido pedido = Pedido.rascunho(clienteId);
        pedido.alterarInfoEntrega(entrega);
        pedido.alterarInfoCobranca(cobranca);
        pedido.alterarMetodoPagamento(metodoPagamento);

        if (comItens) {
            pedido.adicionaItemPedido(
                    ProdutoTestDataBuilder.umProduto().build(),
                    new Quantidade(2));

            pedido.adicionaItemPedido(
                    ProdutoTestDataBuilder.umProduto().build(),
                    new Quantidade(1));
        }

        switch (this.status) {
            case RASCUNHO -> {
            }
            case REALIZADO -> {
                pedido.realizar();
            }
            case PAGO -> {
                pedido.realizar();
                pedido.marcaPago();
            }
            case PRONTO -> {
                pedido.realizar();
                pedido.marcaPago();
                pedido.marcaPronto();
            }
            case CANCELADO -> {
                pedido.cancelar();
            }
        }

        return pedido;
    }

    public static Recebedor umRecebedor() {
        return Recebedor.builder()
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911")).build();
    }

    public static Cobranca umaCobranca() {
        return Cobranca.builder()
                .endereco(umEndereco())
                .recebedor(umRecebedor())
                .build();
    }

    public static Entrega umaEntrega() {
        return Entrega.builder()
                .endereco(umEndereco())
                .recebedor(umRecebedor())
                .custo(new Dinheiro("10.00"))
                .dataPrevista(LocalDate.now().plusWeeks(1))
                .build();
    }

    public static Endereco umEndereco() {
        return Endereco.builder()
                .rua("Rua Bourbon")
                .numero("1234")
                .bairro("North Ville")
                .complemento("apt. 11")
                .cidade("Montfort")
                .estado("South Carolina")
                .cep(new CEP("79911")).build();
    }

    public PedidoTestDataBuilder clienteId(ClienteId clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public PedidoTestDataBuilder metodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
        return this;
    }

    public PedidoTestDataBuilder entrega(Entrega entrega) {
        this.entrega = entrega;
        return this;
    }

    public PedidoTestDataBuilder cobranca(Cobranca cobranca) {
        this.cobranca = cobranca;
        return this;
    }

    public PedidoTestDataBuilder comItens(boolean comItens) {
        this.comItens = comItens;
        return this;
    }

    public PedidoTestDataBuilder status(StatusPedido status) {
        this.status = status;
        return this;
    }

}
