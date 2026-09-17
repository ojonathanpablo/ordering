package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.PedidoDataEntregaInvalidaException;
import com.shop.ordering.domain.exception.PedidoNaoComtenItemException;
import com.shop.ordering.domain.exception.PedidoNaoPodeSerRealizadoException;
import com.shop.ordering.domain.exception.StatusPedidoNaoPodeSerAlterado;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import com.shop.ordering.domain.valueobject.id.ItemPedidoId;
import com.shop.ordering.domain.valueobject.id.PedidoId;
import lombok.Builder;

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

    private Cobranca cobranca;
    private Entrega entrega;

    private StatusPedido statusPedido;
    private MetodoPagamento metodoPagamento;

    private Set<ItemPedido> itensPedido;

    @Builder(builderClassName = "PedidoExistenteBuilder", buildMethodName = "existente")
    public Pedido(PedidoId id, ClienteId clienteId, Dinheiro valorTotal, Quantidade totalItens, OffsetDateTime realizadoEm, OffsetDateTime pagoEm, OffsetDateTime canceladoEm, OffsetDateTime prontoEm, Cobranca cobranca, Entrega entrega, StatusPedido status, MetodoPagamento metodoPagamento, Set<ItemPedido> itens) {

        this.setId(id);
        this.setClienteId(clienteId);
        this.setValorTotal(valorTotal);
        this.setQuantidade(totalItens);
        this.setRealizadoEm(realizadoEm);
        this.setPagoEm(pagoEm);
        this.setCanceladoEm(canceladoEm);
        this.setProntoEm(prontoEm);
        this.setCobranca(cobranca);
        this.setEntrega(entrega);
        this.setStatusPedido(status);
        this.setMetodoPagamento(metodoPagamento);
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
                new HashSet<>());
    }

    public void adicionaItemPedido(Produto produto,
                                   Quantidade quantidade) {
        Objects.requireNonNull(produto);
        Objects.requireNonNull(quantidade);

        produto.produtoEmEstoque();

        ItemPedido itemPedido = ItemPedido.novo()
                .pedidoId(this.id())
                .produto(produto)
                .quantidade(quantidade)
                .build();

        if (this.itensPedido == null) {
            this.itensPedido = new HashSet<>();
        }

        this.itensPedido.add(itemPedido);

        recalcularTotais();

    }

    public void realizar() {
        this.verificarPodeRealizar();

        this.setRealizadoEm(OffsetDateTime.now());
        this.mudarStatus(StatusPedido.REALIZADO);
    }

    public void marcaPago() {
        this.setPagoEm(OffsetDateTime.now());
        this.mudarStatus(StatusPedido.PAGO);
    }

    public void alterarQuantidadeItem(ItemPedidoId itemPedidoId, Quantidade quantidade) {
        Objects.requireNonNull(itemPedidoId);
        Objects.requireNonNull(quantidade);

        ItemPedido itemPedido = localizarItemPedido(itemPedidoId);
        itemPedido.alterarQuantidade(quantidade);

        this.recalcularTotais();

    }

    public void alterarMetodoPagamento(MetodoPagamento metodoPagamento) {
        Objects.requireNonNull(metodoPagamento);
        this.setMetodoPagamento(metodoPagamento);
    }

    public void alterarInfoCobranca(Cobranca cobranca) {
        Objects.requireNonNull(cobranca);
        this.setCobranca(cobranca);
    }

    public void alterarInfoEntrega(Entrega entrega) {
        Objects.requireNonNull(entrega);

        if (entrega.dataPrevista().isBefore(LocalDate.now())) {
            throw new PedidoDataEntregaInvalidaException(this.id(), entrega.dataPrevista());
        }

        this.setEntrega(entrega);
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

    public Cobranca cobranca() {
        return cobranca;
    }

    public Entrega entrega() {
        return entrega;
    }

    public StatusPedido statusPedido() {
        return statusPedido;
    }

    public MetodoPagamento metodoPagamento() {
        return metodoPagamento;
    }

    public Set<ItemPedido> itensPedido() {
        return Collections.unmodifiableSet(this.itensPedido);
    }

    private void recalcularTotais() {
        BigDecimal valorTotalItens = this.itensPedido.stream().map(i -> i.valorTotal().valor()).reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalQuantidadeItens = this.itensPedido.stream().map(i -> i.quantidade().valor()).reduce(0, Integer::sum);

        BigDecimal custoEntrega;
        if (this.entrega() == null) {
            custoEntrega = BigDecimal.ZERO;
        } else {
            custoEntrega = this.entrega.custo().valor();
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

    private void verificarPodeRealizar() {
        if (this.entrega() == null) {
            throw PedidoNaoPodeSerRealizadoException.semInfoEntrega(this.id());
        }

        if (this.cobranca() == null) {
            throw PedidoNaoPodeSerRealizadoException.semInfoCobranca(this.id());
        }

        if (this.metodoPagamento() == null) {
            throw PedidoNaoPodeSerRealizadoException.semMetodoPagamento(this.id());
        }

        if (this.itensPedido().isEmpty()) {
            throw PedidoNaoPodeSerRealizadoException.semItens(this.id());
        }
    }

    private ItemPedido localizarItemPedido(ItemPedidoId itemPedidoId) {
        Objects.requireNonNull(itemPedidoId);

        return this.itensPedido()
                .stream()
                .filter(i -> i.itemPedidoId().equals(itemPedidoId))
                .findFirst()
                .orElseThrow(() -> new PedidoNaoComtenItemException(this.id, itemPedidoId));
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

    private void setCobranca(Cobranca cobranca) {
        this.cobranca = cobranca;
    }

    private void setEntrega(Entrega entrega) {
        this.entrega = entrega;
    }

    private void setStatusPedido(StatusPedido statusPedido) {
        Objects.requireNonNull(statusPedido);
        this.statusPedido = statusPedido;
    }

    private void setMetodoPagamento(MetodoPagamento metodoPagamento) {
        this.metodoPagamento = metodoPagamento;
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
