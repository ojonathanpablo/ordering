package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.ProdutoIncompativelNoCarrinhoException;
import com.shop.ordering.domain.valueobject.Dinheiro;
import com.shop.ordering.domain.valueobject.NomeProduto;
import com.shop.ordering.domain.valueobject.Produto;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.valueobject.id.ItemCarrinhoComprasId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;
import lombok.Builder;

import java.util.Objects;

public class ItemCarrinhoCompras {

    private ItemCarrinhoComprasId itemCarrinhoComprasId;
    private CarrinhoComprasId carrinhoComprasId;
    private ProdutoId produtoId;
    private NomeProduto nomeProduto;
    private Dinheiro preco;
    private Quantidade quantidade;
    private Dinheiro valorTotal;
    private Boolean disponivel;

    @Builder(builderClassName = "ItemCarrinhoComprasExistenteBuilder", builderMethodName = "existente")
    public ItemCarrinhoCompras(ItemCarrinhoComprasId itemCarrinhoComprasId,
                               CarrinhoComprasId carrinhoComprasId,
                               ProdutoId produtoId,
                               NomeProduto nomeProduto,
                               Dinheiro preco,
                               Quantidade quantidade,
                               Dinheiro valorTotal,
                               Boolean disponivel) {

        this.setItemCarrinhoComprasId(itemCarrinhoComprasId);
        this.setCarrinhoComprasId(carrinhoComprasId);
        this.setProdutoId(produtoId);
        this.setNomeProduto(nomeProduto);
        this.setPreco(preco);
        this.setQuantidade(quantidade);
        this.setValorTotal(valorTotal);
        this.setDisponivel(disponivel);
    }

    @Builder(builderClassName = "ItemCarrinhoNovoBuild", builderMethodName = "novo")
    private static ItemCarrinhoCompras criarNovo(CarrinhoComprasId carrinhoComprasId,
                                                 Produto produto,
                                                 Quantidade quantidade) {

        ItemCarrinhoCompras itemCarrinhoCompras = new ItemCarrinhoCompras(
                new ItemCarrinhoComprasId(),
                carrinhoComprasId,
                produto.produtoId(),
                produto.nomeProduto(),
                produto.preco(),
                quantidade,
                Dinheiro.ZERO,
                produto.emEstoque()
        );

        itemCarrinhoCompras.recalcularTotais();

        return itemCarrinhoCompras;
    }

    void atualizar(Produto produto) {
        Objects.requireNonNull(produto);
        Objects.requireNonNull(produto.produtoId());

        if (!produto.produtoId().equals(this.produtoId())) {
            throw new ProdutoIncompativelNoCarrinhoException(this.carrinhoComprasId(), produto.produtoId());
        }

        this.setPreco(produto.preco());
        this.setDisponivel(produto.emEstoque());
        this.setNomeProduto(produto.nomeProduto());

        this.recalcularTotais();

    }

    void alterarQuantidade(Quantidade quantidade) {
        Objects.requireNonNull(quantidade);
        if (quantidade.valor() == 0) {
            throw new IllegalArgumentException();
        }
        this.setQuantidade(quantidade);
        this.recalcularTotais();
    }

    public ItemCarrinhoComprasId itemCarrinhoComprasId() {
        return itemCarrinhoComprasId;
    }

    public CarrinhoComprasId carrinhoComprasId() {
        return carrinhoComprasId;
    }

    public ProdutoId produtoId() {
        return produtoId;
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

    public Boolean disponivel() {
        return disponivel;
    }

    private void recalcularTotais() {
        this.setValorTotal(this.preco().multiplicar(this.quantidade));
    }

    private void setItemCarrinhoComprasId(ItemCarrinhoComprasId itemCarrinhoComprasId) {
        Objects.requireNonNull(itemCarrinhoComprasId);
        this.itemCarrinhoComprasId = itemCarrinhoComprasId;
    }

    private void setCarrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        Objects.requireNonNull(carrinhoComprasId);
        this.carrinhoComprasId = carrinhoComprasId;
    }

    private void setProdutoId(ProdutoId produtoId) {
        Objects.requireNonNull(produtoId);
        this.produtoId = produtoId;
    }

    private void setNomeProduto(NomeProduto nomeProduto) {
        Objects.requireNonNull(nomeProduto);
        this.nomeProduto = nomeProduto;
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

    private void setDisponivel(Boolean disponivel) {
        Objects.requireNonNull(disponivel);
        this.disponivel = disponivel;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        ItemCarrinhoCompras that = (ItemCarrinhoCompras) o;
        return Objects.equals(itemCarrinhoComprasId, that.itemCarrinhoComprasId);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(itemCarrinhoComprasId);
    }
}
