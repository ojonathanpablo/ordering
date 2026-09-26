package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.CarrinhoComprasNaoContemItemException;
import com.shop.ordering.domain.model.exception.CarrinhoComprasNaoContemProdutoException;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.ItemCarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

public class CarrinhoCompras implements RaizDeAgregado<CarrinhoComprasId> {

    private CarrinhoComprasId carrinhoComprasId;
    private ClienteId clienteId;
    private Dinheiro valorTotal;
    private Quantidade totalDeItens;
    private OffsetDateTime criadoEm;
    private Set<ItemCarrinhoCompras> itens;
    private Long versao;

    @Builder(builderClassName = "CarrinhoComprasExistenteBuilder", builderMethodName = "existente")
    public CarrinhoCompras(CarrinhoComprasId carrinhoComprasId,
                           ClienteId clienteId,
                           Dinheiro valorTotal,
                           Quantidade totalDeItens,
                           OffsetDateTime criadoEm,
                           Set<ItemCarrinhoCompras> itens,
                           Long versao) {

        this.setCarrinhoComprasId(carrinhoComprasId);
        this.setClienteId(clienteId);
        this.setValorTotal(valorTotal);
        this.setTotalDeItens(totalDeItens);
        this.setCriadoEm(criadoEm);
        this.setItens(itens);
        this.setVersao(versao);
    }

    @Builder(builderClassName = "CarrinhoNovoBuild", builderMethodName = "novo")
    public static CarrinhoCompras criarNovo(ClienteId clienteId) {

        return new CarrinhoCompras(
                new CarrinhoComprasId(),
                clienteId,
                Dinheiro.ZERO,
                Quantidade.ZERO,
                OffsetDateTime.now(),
                new HashSet<>(),
                null
        );

    }

    public void limpar() {
        Set<ItemCarrinhoComprasId> idsItens = new HashSet<>(
                this.itens().stream().map(ItemCarrinhoCompras::itemCarrinhoComprasId).toList()
        );

        idsItens.forEach(this::removerItem);
    }

    public void removerItem(ItemCarrinhoComprasId itemCarrinhoComprasId) {
        ItemCarrinhoCompras itemCarrinhoCompras = this.encontrarItem(itemCarrinhoComprasId);
        this.itens.remove(itemCarrinhoCompras);
        this.recalcularTotais();
    }

    public void adicionarItem(Produto produto, Quantidade quantidade) {
        Objects.requireNonNull(produto);
        Objects.requireNonNull(quantidade);

        produto.produtoEmEstoque();

        ItemCarrinhoCompras itemCarrinhoCompras = ItemCarrinhoCompras.novo()
                .carrinhoComprasId(this.id())
                .produto(produto)
                .quantidade(quantidade)
                .build();

        buscarItemPorProduto(produto.produtoId())
                .ifPresentOrElse(
                        i -> atualizarItemExistente(i, produto, quantidade), () -> inserirItem(itemCarrinhoCompras));

        this.recalcularTotais();
    }

    public void atualizarItem(Produto produto) {
        ItemCarrinhoCompras itemCarrinhoCompras = this.encontrarItem(produto.produtoId());
        itemCarrinhoCompras.atualizar(produto);
        this.recalcularTotais();
    }

    public ItemCarrinhoCompras encontrarItem(ItemCarrinhoComprasId itemCarrinhoComprasId) {
        Objects.requireNonNull(itemCarrinhoComprasId);
        return this.itens.stream()
                .filter(i -> i.itemCarrinhoComprasId().equals(itemCarrinhoComprasId))
                .findFirst()
                .orElseThrow(() -> new CarrinhoComprasNaoContemItemException(this.id(), itemCarrinhoComprasId));
    }

    public ItemCarrinhoCompras encontrarItem(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        return this.itens.stream()
                .filter(i -> i.produtoId().equals(produtoId))
                .findFirst()
                .orElseThrow(() -> new CarrinhoComprasNaoContemProdutoException(this.id(), produtoId));
    }

    public void alterarQuantidadeDoItem(ItemCarrinhoComprasId itemCarrinhoComprasId, Quantidade quantidade) {
        ItemCarrinhoCompras itemCarrinhoCompras = this.encontrarItem(itemCarrinhoComprasId);
        itemCarrinhoCompras.alterarQuantidade(quantidade);
        this.recalcularTotais();
    }

    public boolean contemItensIndisponiveis() {
        return itens.stream().anyMatch(i -> !i.disponivel());
    }

    public boolean estaVazio() {
        return this.itens().isEmpty();
    }

    public Quantidade totalDeItens() {
        return totalDeItens;
    }

    public CarrinhoComprasId id() {
        return carrinhoComprasId;
    }

    public Dinheiro valorTotal() {
        return valorTotal;
    }

    public ClienteId clienteId() {
        return clienteId;
    }

    public OffsetDateTime criadoEm() {
        return criadoEm;
    }

    public Set<ItemCarrinhoCompras> itens() {
        return Collections.unmodifiableSet(itens);
    }

    public Long versao() {
        return versao;
    }

    private void atualizarItemExistente(ItemCarrinhoCompras itemCarrinhoCompras, Produto produto, Quantidade quantidade) {
        itemCarrinhoCompras.atualizar(produto);
        itemCarrinhoCompras.alterarQuantidade(itemCarrinhoCompras.quantidade().somar(quantidade));
    }

    private void inserirItem(ItemCarrinhoCompras itemCarrinhoCompras) {
        this.itens.add(itemCarrinhoCompras);
    }

    private Optional<ItemCarrinhoCompras> buscarItemPorProduto(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        return this.itens.stream()
                .filter(i -> i.produtoId().equals(produtoId))
                .findFirst();
    }

    private void recalcularTotais() {
        BigDecimal valorTotal = itens.stream()
                .map(i -> i.valorTotal().valor())
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Integer totalDeItens = itens.stream()
                .map(i -> i.quantidade().valor())
                .reduce(0, Integer::sum);

        this.setValorTotal(new Dinheiro(valorTotal));
        this.setTotalDeItens(new Quantidade(totalDeItens));
    }

    private void setClienteId(ClienteId clienteId) {
        Objects.requireNonNull(clienteId);
        this.clienteId = clienteId;
    }

    private void setCarrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        Objects.requireNonNull(carrinhoComprasId);
        this.carrinhoComprasId = carrinhoComprasId;
    }

    private void setValorTotal(Dinheiro valorTotal) {
        Objects.requireNonNull(valorTotal);
        this.valorTotal = valorTotal;
    }

    private void setTotalDeItens(Quantidade totalDeItens) {
        Objects.requireNonNull(totalDeItens);
        this.totalDeItens = totalDeItens;
    }

    private void setCriadoEm(OffsetDateTime criadoEm) {
        Objects.requireNonNull(criadoEm);
        this.criadoEm = criadoEm;
    }

    private void setItens(Set<ItemCarrinhoCompras> itens) {
        Objects.requireNonNull(itens);
        this.itens = itens;
    }

    private void setVersao(Long versao) {
        this.versao = versao;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CarrinhoCompras that = (CarrinhoCompras) o;
        return Objects.equals(carrinhoComprasId, that.carrinhoComprasId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(carrinhoComprasId);
    }
}
