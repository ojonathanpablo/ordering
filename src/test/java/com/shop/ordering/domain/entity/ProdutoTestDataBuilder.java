package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.Dinheiro;
import com.shop.ordering.domain.valueobject.NomeProduto;
import com.shop.ordering.domain.valueobject.Produto;
import com.shop.ordering.domain.valueobject.id.ProdutoId;

public class ProdutoTestDataBuilder {

    public static Produto.ProdutoBuilder umProduto() {
        return Produto.builder()
                .produtoId(new ProdutoId())
                .emEstoque(true)
                .nomeProduto(new NomeProduto("Notebook X11"))
                .preco(new Dinheiro("3000"));
    }

    public static Produto.ProdutoBuilder umProdutoIndisponivel() {
        return Produto.builder()
                .produtoId(new ProdutoId())
                .nomeProduto(new NomeProduto("Desktop FX9000"))
                .preco(new Dinheiro("5000"))
                .emEstoque(false);
    }

    public static Produto.ProdutoBuilder umProdutoAltMemoriaRam() {
        return Produto.builder()
                .produtoId(new ProdutoId())
                .nomeProduto(new NomeProduto("8GB RAM"))
                .preco(new Dinheiro("300"))
                .emEstoque(true);
    }

}
