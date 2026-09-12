package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.Dinheiro;
import com.algashop.ordering.domain.valueobject.NomeProduto;
import com.algashop.ordering.domain.valueobject.Quantidade;
import com.algashop.ordering.domain.valueobject.id.PedidoId;
import com.algashop.ordering.domain.valueobject.id.ItemPedidoId;
import com.algashop.ordering.domain.valueobject.id.ProdutoId;
import lombok.Builder;

import java.util.Objects;

public class ItemPedido {

    private ItemPedidoId id;
    private PedidoId pedidoId;

    private ProdutoId produtoId;
    private NomeProduto nomeProduto;

    private Dinheiro preco;
    private Quantidade quantidade;

    private Dinheiro valorTotal;

    @Builder(builderClassName = "ItemPedidoExistenteBuilder", builderMethodName = "existente")
    public ItemPedido(ItemPedidoId id, PedidoId pedidoId,
                     ProdutoId produtoId, NomeProduto nomeProduto,
                     Dinheiro preco, Quantidade quantidade,
                     Dinheiro valorTotal) {
        this.setId(id);
        this.setPedidoId(pedidoId);
        this.setProdutoId(produtoId);
        this.setNomeProduto(nomeProduto);
        this.setPreco(preco);
        this.setQuantidade(quantidade);
        this.setValorTotal(valorTotal);
    }

    @Builder(builderClassName = "ItemPedidoNovoBuilder", builderMethodName = "novo")
    private static ItemPedido criarNovo(PedidoId id,
                                     ProdutoId produtoId, NomeProduto nomeProduto,
                                     Dinheiro preco, Quantidade quantidade) {
        return new ItemPedido(
                new ItemPedidoId(),
                id,
                produtoId,
                nomeProduto,
                preco,
                quantidade,
                Dinheiro.ZERO
        );
    }

    public ProdutoId produtoId() {
        return produtoId;
    }

    public ItemPedidoId itemPedidoId() {
        return id;
    }

    public PedidoId pedidoId() {
        return pedidoId;
    }

    public NomeProduto nomeProduto() {
        return nomeProduto;
    }

    public Dinheiro preco() {
        return preco;
    }

    public Quantidade quantidade() {
        return quantidade;
    }

    public Dinheiro valorTotal() {
        return valorTotal;
    }

    private void setNomeProduto(NomeProduto nomeProduto) {
        Objects.requireNonNull(nomeProduto);
        this.nomeProduto = nomeProduto;
    }

    private void setId(ItemPedidoId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setPedidoId(PedidoId pedidoId) {
        Objects.requireNonNull(pedidoId);
        this.pedidoId = pedidoId;
    }

    private void setProdutoId(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        this.produtoId = produtoId;
    }

    private void setPreco(Dinheiro preco) {
        Objects.requireNonNull(preco);
        this.preco = preco;
    }

    private void setQuantidade(Quantidade quantidade) {
        Objects.requireNonNull(quantidade);
        this.quantidade = quantidade;
    }

    private void setValorTotal(Dinheiro valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemPedido itemPedido = (ItemPedido) o;
        return Objects.equals(id, itemPedido.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
