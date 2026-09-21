package com.shop.ordering.domain.model.valueobject;

import com.shop.ordering.domain.model.exception.ProdutoEsgotadoException;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;
import lombok.Builder;

import java.util.Objects;

@Builder
public record Produto(
        ProdutoId produtoId,
        NomeProduto nomeProduto,
        Dinheiro preco,
        Boolean emEstoque) {

    public Produto {
        Objects.requireNonNull(produtoId);
        Objects.requireNonNull(nomeProduto);
        Objects.requireNonNull(preco);
        Objects.requireNonNull(emEstoque);

    }

    public void produtoEmEstoque(){
        if (isEsgotado()){
            throw new ProdutoEsgotadoException(this.produtoId());
        }
    }

    public boolean isEsgotado() {
        return !emEstoque();
    }

}

