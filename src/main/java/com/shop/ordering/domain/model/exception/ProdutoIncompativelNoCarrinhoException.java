package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;

import static com.shop.ordering.domain.model.exception.MensagensErro.ERRO_CARRINHO_COMPRAS_ITEM_PRODUTO_INCOMPATIVEL;

public class ProdutoIncompativelNoCarrinhoException extends ExcecaoDominio {

    public ProdutoIncompativelNoCarrinhoException(CarrinhoComprasId carrinhoComprasId, ProdutoId produtoId) {
        super(String.format(ERRO_CARRINHO_COMPRAS_ITEM_PRODUTO_INCOMPATIVEL, carrinhoComprasId, produtoId));
    }
}
