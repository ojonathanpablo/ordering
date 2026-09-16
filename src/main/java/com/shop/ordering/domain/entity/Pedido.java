package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.InfoCobranca;
import com.shop.ordering.domain.valueobject.Dinheiro;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.InfoEntrega;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.PedidoId;
import lombok.Builder;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class Pedido {

    private PedidoId id;
    private ClienteId clienteId;

    private Dinheiro valorTotal;
    private Quantidade quantidade;

    private OffsetDateTime realizadoEm;
    private OffsetDateTime pagoEm;
    private OffsetDateTime canceladoEm;
    private OffsetDateTime prontoEm;

    private InfoCobranca infoCobranca;
    private InfoEntrega infoEntrega;

    private StatusPedido statusPedido;
    private MetodoPagamento metodoPagamento;

    private Dinheiro custoEntrega;
    private LocalDate dataEntregaPrevista;

    private Set<ItemPedido> itensPedido;

    @Builder(builderClassName = "PedidoExistenteBuilder", buildMethodName = "existente")
    public Pedido(PedidoId id, ClienteId clienteId,
                 Dinheiro valorTotal, Quantidade totalItens,
                 OffsetDateTime realizadoEm, OffsetDateTime pagoEm,
                 OffsetDateTime canceladoEm, OffsetDateTime prontoEm,
                 InfoCobranca cobranca, InfoEntrega entrega,
                 StatusPedido status, MetodoPagamento metodoPagamento,
                 Dinheiro custoEntrega, LocalDate dataEntregaPrevista,
                 Set<ItemPedido> itens) {

        this.setId(id);
        this.setClienteId(clienteId);
        this.setValorTotal(valorTotal);
        this.setQuantidade(totalItens);
        this.setRealizadoEm(realizadoEm);
        this.setPagoEm(pagoEm);
        this.setCanceladoEm(canceladoEm);
        this.setProntoEm(prontoEm);
        this.setInfoCobranca(cobranca);
        this.setInfoEntrega(entrega);
        this.setStatusPedido(status);
        this.setMetodoPagamento(metodoPagamento);
        this.setCustoEntrega(custoEntrega);
        this.setDataEntregaPrevista(dataEntregaPrevista);
        this.setItensPedido(itens);
    }

    public static Pedido rascunho(ClienteId clienteId) {
        return new Pedido(
                new PedidoId(),
                clienteId,
                Dinheiro.ZERO,
                Quantidade.ZERO,
                null,
                null,
                null,
                null,
                null,
                null,
                StatusPedido.RASCUNHO,
                null,
                null,
                null,
                new HashSet<>()
        );
    }

    public OffsetDateTime realizadoEm() {
        return realizadoEm;
    }

    public PedidoId id() {
        return id;
    }

    public ClienteId clienteId() {
        return clienteId;
    }

    public Dinheiro valorTotal() {
        return valorTotal;
    }

    public Quantidade quantidade() {
        return quantidade;
    }

    public OffsetDateTime pagoEm() {
        return pagoEm;
    }

    public OffsetDateTime canceladoEm() {
        return canceladoEm;
    }

    public OffsetDateTime prontoEm() {
        return prontoEm;
    }

    public InfoCobranca infoCobranca() {
        return infoCobranca;
    }

    public InfoEntrega infoEntrega() {
        return infoEntrega;
    }

    public StatusPedido statusPedido() {
        return statusPedido;
    }

    public MetodoPagamento metodoPagamento() {
        return metodoPagamento;
    }

    public Dinheiro custoEntrega() {
        return custoEntrega;
    }

    public LocalDate dataEntregaPrevista() {
        return dataEntregaPrevista;
    }

    public Set<ItemPedido> itensPedido() {
        return itensPedido;
    }

    private void setClienteId(ClienteId clienteId) {
        Objects.requireNonNull(clienteId);
        this.clienteId = clienteId;
    }

    private void setId(PedidoId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setValorTotal(Dinheiro valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    private void setQuantidade(Quantidade quantidade) {
        Objects.requireNonNull(quantidade);
        this.quantidade = quantidade;
    }

    private void setRealizadoEm(OffsetDateTime realizadoEm) {
        this.realizadoEm = realizadoEm;
    }

    private void setPagoEm(OffsetDateTime pagoEm) {
        this.pagoEm = pagoEm;
    }

    private void setCanceladoEm(OffsetDateTime canceladoEm) {
        this.canceladoEm = canceladoEm;
    }

    private void setProntoEm(OffsetDateTime prontoEm) {
        this.prontoEm = prontoEm;
    }

    private void setInfoCobranca(InfoCobranca infoCobranca) {
        this.infoCobranca = infoCobranca;
    }

    private void setInfoEntrega(InfoEntrega infoEntrega) {
        this.infoEntrega = infoEntrega;
    }

    private void setStatusPedido(StatusPedido statusPedido) {
        Objects.requireNonNull(statusPedido);
        this.statusPedido = statusPedido;
    }

    private void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
    }

    private void setCustoEntrega(Dinheiro custoEntrega) {
        this.custoEntrega = custoEntrega;
    }

    private void setDataEntregaPrevista(LocalDate dataEntregaPrevista) {
        this.dataEntregaPrevista = dataEntregaPrevista;
    }

    private void setItensPedido(Set<ItemPedido> itensPedido) {
        Objects.requireNonNull(itensPedido);
        this.itensPedido = itensPedido;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pedido pedido = (Pedido) o;
        return Objects.equals(id, pedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
