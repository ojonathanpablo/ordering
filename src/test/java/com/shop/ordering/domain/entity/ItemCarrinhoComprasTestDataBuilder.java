package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.Produto;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.CarrinhoComprasId;

public class ItemCarrinhoComprasTestDataBuilder {

    private CarrinhoComprasId carrinhoComprasId = CarrinhoComprasTestDataBuilder.ID_CARRINHO_PADRAO;
    private Produto produto = ProdutoTestDataBuilder.umProduto().build();
    private Quantidade quantidade = new Quantidade(1);

    private ItemCarrinhoComprasTestDataBuilder() {
    }

    public static ItemCarrinhoComprasTestDataBuilder umItemCarrinhoCompras() {
        return new ItemCarrinhoComprasTestDataBuilder();
    }

    public ItemCarrinhoCompras build() {
        return ItemCarrinhoCompras.novo()
                .carrinhoComprasId(carrinhoComprasId)
                .produto(produto)
                .quantidade(quantidade)
                .build();
    }

    public ItemCarrinhoComprasTestDataBuilder carrinhoComprasId(CarrinhoComprasId carrinhoComprasId) {
        this.carrinhoComprasId = carrinhoComprasId;
        return this;
    }

    public ItemCarrinhoComprasTestDataBuilder produto(Produto produto) {
        this.produto = produto;
        return this;
    }

    public ItemCarrinhoComprasTestDataBuilder quantidade(Quantidade quantidade) {
        this.quantidade = quantidade;
        return this;
    }

}
