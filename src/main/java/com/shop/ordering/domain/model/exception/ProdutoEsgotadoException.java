package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.valueobject.id.ProdutoId;

public class ProdutoEsgotadoException extends ExcecaoDominio {

    public ProdutoEsgotadoException(ProdutoId produtoId) {
        super(String.format(MensagensErro.ERRO_PRODUTO_FORA_DE_ESTOQUE, produtoId));
    }
}
