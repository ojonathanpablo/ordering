package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;

import java.time.LocalDate;

public class PedidoTestDataBuilder {

    private ClienteId clienteId = new ClienteId();

    private MetodoPagamento metodoPagamento = MetodoPagamento.SALDO_GATEWAY;

    private Dinheiro custoEntrega = new Dinheiro("10.00");
    private LocalDate dataEntregaPrevista = LocalDate.now().plusWeeks(1);

    private InfoEntrega infoEntrega = umaInfoEntrega();
    private InfoCobranca infoCobranca = umaInfoCobranca();

    private boolean comItens = true;

    private StatusPedido status = StatusPedido.RASCUNHO;

    private PedidoTestDataBuilder() {

    }

    public static PedidoTestDataBuilder umPedido() {
        return new PedidoTestDataBuilder();
    }

    public Pedido build() {
        Pedido pedido = Pedido.rascunho(clienteId);
        pedido.alterarInfoEntrega(infoEntrega, custoEntrega, dataEntregaPrevista);
        pedido.alterarInfoCobranca(infoCobranca);
        pedido.alterarMetodoPagamento(metodoPagamento);

        if (comItens) {
            pedido.adicionaItemPedido(new ProdutoId(), new NomeProduto("Notebook X11"),
                    new Dinheiro("3000"), new Quantidade(2));

            pedido.adicionaItemPedido(new ProdutoId(), new NomeProduto("4GB RAM"),
                    new Dinheiro("200"), new Quantidade(1));
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
            }
            case CANCELADO -> {
            }
        }

        return pedido;
    }

    public static InfoCobranca umaInfoCobranca() {
        return InfoCobranca.builder()
                .endereco(umEndereco())
                .documento(new Documento("225-09-1992"))
                .telefone(new Telefone("123-111-9911"))
                .nomeCompleto(new NomeCompleto("John", "Doe")).build();
    }

    public static InfoEntrega umaInfoEntrega() {
        return InfoEntrega.builder()
                .endereco(umEndereco())
                .nomeCompleto(new NomeCompleto("John", "Doe"))
                .documento(new Documento("112-33-2321"))
                .telefone(new Telefone("111-441-1244")).build();
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

    public PedidoTestDataBuilder custoEntrega(Dinheiro custoEntrega) {
        this.custoEntrega = custoEntrega;
        return this;
    }

    public PedidoTestDataBuilder dataEntregaPrevista(LocalDate dataEntregaPrevista) {
        this.dataEntregaPrevista = dataEntregaPrevista;
        return this;
    }

    public PedidoTestDataBuilder infoEntrega(InfoEntrega infoEntrega) {
        this.infoEntrega = infoEntrega;
        return this;
    }

    public PedidoTestDataBuilder infoCobranca(InfoCobranca infoCobranca) {
        this.infoCobranca = infoCobranca;
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
