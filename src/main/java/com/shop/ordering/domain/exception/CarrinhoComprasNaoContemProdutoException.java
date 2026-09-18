package com.shop.ordering.domain.exception;

import com.shop.ordering.domain.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;

import static com.shop.ordering.domain.exception.MensagensErro.ERRO_CARRINHO_COMPRAS_NAO_CONTEM_PRODUTO;

public class CarrinhoComprasNaoContemProdutoException extends ExcecaoDominio {

    public CarrinhoComprasNaoContemProdutoException(CarrinhoComprasId carrinhoComprasId, ProdutoId produtoId) {
        super(String.format(ERRO_CARRINHO_COMPRAS_NAO_CONTEM_PRODUTO, carrinhoComprasId, produtoId));
    }
}
