package com.shop.ordering.domain.model.exception;

import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ItemCarrinhoComprasId;

import static com.shop.ordering.domain.model.exception.MensagensErro.ERRO_CARRINHO_COMPRAS_NAO_CONTEM_ITEM;

public class CarrinhoComprasNaoContemItemException extends ExcecaoDominio {

    public CarrinhoComprasNaoContemItemException(CarrinhoComprasId carrinhoComprasId, ItemCarrinhoComprasId itemCarrinhoComprasId) {
        super(String.format(ERRO_CARRINHO_COMPRAS_NAO_CONTEM_ITEM, carrinhoComprasId, itemCarrinhoComprasId));
    }
}
