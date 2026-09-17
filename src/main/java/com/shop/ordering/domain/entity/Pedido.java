package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.PedidoDataEntregaInvalidaException;
import com.shop.ordering.domain.exception.PedidoNaoPodeSerRealizadoException;
import com.shop.ordering.domain.exception.StatusPedidoNaoPodeSerAlterado;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.PedidoId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;
import lombok.Builder;
import org.apache.commons.validator.routines.DomainValidator;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.*;

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
    public Pedido(PedidoId id, ClienteId clienteId, Dinheiro valorTotal, Quantidade totalItens, OffsetDateTime realizadoEm, OffsetDateTime pagoEm, OffsetDateTime canceladoEm, OffsetDateTime prontoEm, InfoCobranca cobranca, InfoEntrega entrega, StatusPedido status, MetodoPagamento metodoPagamento, Dinheiro custoEntrega, LocalDate dataEntregaPrevista, Set<ItemPedido> itens) {

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
        return new Pedido(new PedidoId(), clienteId, Dinheiro.ZERO, Quantidade.ZERO, null, null, null, null, null, null, StatusPedido.RASCUNHO, null, null, null, new HashSet<>());
    }

    public void adicionaItemPedido(ProdutoId produtoId, NomeProduto nomeProduto, Dinheiro preco, Quantidade quantidade) {

        ItemPedido itemPedido = ItemPedido.novo().id(this.id).preco(preco).quantidade(quantidade).nomeProduto(nomeProduto).produtoId(produtoId).build();

        if (this.itensPedido == null) {
            this.itensPedido = new HashSet<>();
        }

        this.itensPedido.add(itemPedido);

        recalcularTotais();

    }

    public void realizar() {
        Objects.requireNonNull(this.infoEntrega());
        Objects.requireNonNull(this.infoCobranca());
        Objects.requireNonNull(this.dataEntregaPrevista());
        Objects.requireNonNull(this.custoEntrega());
        Objects.requireNonNull(this.metodoPagamento());
        Objects.requireNonNull(this.itensPedido());

        if (this.itensPedido().isEmpty()) {
            throw new PedidoNaoPodeSerRealizadoException(this.id());
        }

        this.setRealizadoEm(OffsetDateTime.now());
        this.mudarStatus(StatusPedido.REALIZADO);
    }

    public void marcaPago(){
        this.setPagoEm(OffsetDateTime.now());
        this.mudarStatus(StatusPedido.PAGO);
    }

    public void alterarMetodoPagamento(MetodoPagamento metodoPagamento) {
        Objects.requireNonNull(metodoPagamento);
        this.setMetodoPagamento(metodoPagamento);
    }

    public void alterarInfoCobranca(InfoCobranca infoCobranca) {
        Objects.requireNonNull(infoCobranca);
        this.setInfoCobranca(infoCobranca);
    }

    public void alterarInfoEntrega(InfoEntrega infoEntrega, Dinheiro custoEntrega, LocalDate dataEntregaPrevista) {
        Objects.requireNonNull(infoEntrega);
        Objects.requireNonNull(custoEntrega);
        Objects.requireNonNull(dataEntregaPrevista);

        if (dataEntregaPrevista.isBefore(LocalDate.now())) {
            throw new PedidoDataEntregaInvalidaException(this.id(), dataEntregaPrevista);
        }

        this.setInfoEntrega(infoEntrega);
        this.setCustoEntrega(custoEntrega);
        this.setDataEntregaPrevista(dataEntregaPrevista);
    }

    public boolean isRascunho() {
        return StatusPedido.RASCUNHO.equals(this.statusPedido);
    }

    public boolean isRealizado() {
        return StatusPedido.REALIZADO.equals(this.statusPedido);
    }

    public boolean isPago() {
        return StatusPedido.PAGO.equals(this.statusPedido);
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
        return Collections.unmodifiableSet(this.itensPedido);
    }

    private void recalcularTotais() {
        BigDecimal valorTotalItens = this.itensPedido.stream().map(i -> i.valorTotal().valor()).reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantidadeItens = this.itensPedido.stream().map(i -> i.quantidade().valor()).reduce(0, Integer::sum);

        BigDecimal custoEntrega;
        if (this.custoEntrega == null) {
            custoEntrega = BigDecimal.ZERO;
        } else {
            custoEntrega = this.custoEntrega.valor();
        }

        BigDecimal valorTotal = valorTotalItens.add(custoEntrega);

        this.setValorTotal(new Dinheiro(valorTotal));
        this.setQuantidade(new Quantidade(totalQuantidadeItens));

    }

    private void mudarStatus(StatusPedido newStatus) {
        Objects.requireNonNull(newStatus);
        if (this.statusPedido().naoPodeMudarPara(newStatus)) {
            throw new StatusPedidoNaoPodeSerAlterado(this.id, this.statusPedido(), newStatus);
        }
        this.setStatusPedido(newStatus);
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
