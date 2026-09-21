package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.NomeProduto;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;

public class ProdutoTestDataBuilder {

    public static final ProdutoId ID_PRODUTO_PADRAO = new ProdutoId();
    public static final ProdutoId ID_PRODUTO_INDISPONIVEL = new ProdutoId();
    public static final ProdutoId ID_PRODUTO_ALT_MEMORIA_RAM = new ProdutoId();

    public static Produto.ProdutoBuilder umProduto() {
        return Produto.builder()
                .produtoId(ID_PRODUTO_PADRAO)
                .emEstoque(true)
                .nomeProduto(new NomeProduto("Notebook X11"))
                .preco(new Dinheiro("3000"));
    }

    public static Produto.ProdutoBuilder umProdutoIndisponivel() {
        return Produto.builder()
                .produtoId(ID_PRODUTO_INDISPONIVEL)
                .nomeProduto(new NomeProduto("Desktop FX9000"))
                .preco(new Dinheiro("5000"))
                .emEstoque(false);
    }

    public static Produto.ProdutoBuilder umProdutoAltMemoriaRam() {
        return Produto.builder()
                .produtoId(ID_PRODUTO_ALT_MEMORIA_RAM)
                .nomeProduto(new NomeProduto("8GB RAM"))
                .preco(new Dinheiro("300"))
                .emEstoque(true);
    }

}
