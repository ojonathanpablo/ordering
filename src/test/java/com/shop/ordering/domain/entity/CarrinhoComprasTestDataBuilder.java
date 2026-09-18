package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.valueobject.id.ClienteId;

public class CarrinhoComprasTestDataBuilder {

    public static final CarrinhoComprasId ID_CARRINHO_PADRAO = new CarrinhoComprasId();

    private ClienteId clienteId = new ClienteId();
    private boolean comItens = true;

    private CarrinhoComprasTestDataBuilder() {
    }

    public static CarrinhoComprasTestDataBuilder umCarrinhoCompras() {
        return new CarrinhoComprasTestDataBuilder();
    }

    public CarrinhoCompras build() {
        CarrinhoCompras carrinhoCompras = CarrinhoCompras.criarNovo(clienteId);

        if (comItens) {
            carrinhoCompras.adicionarItem(
                    ProdutoTestDataBuilder.umProduto().build(),
                    new Quantidade(2)
            );
            carrinhoCompras.adicionarItem(
                    ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build(),
                    new Quantidade(1)
            );
        }

        return carrinhoCompras;
    }

    public CarrinhoComprasTestDataBuilder clienteId(ClienteId clienteId) {
        this.clienteId = clienteId;
        return this;
    }

    public CarrinhoComprasTestDataBuilder comItens(boolean comItens) {
        this.comItens = comItens;
        return this;
    }

}
