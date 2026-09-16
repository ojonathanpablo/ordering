package com.shop.ordering.domain.entity;


import com.shop.ordering.domain.valueobject.Dinheiro;
import com.shop.ordering.domain.valueobject.NomeProduto;
import com.shop.ordering.domain.valueobject.Quantidade;
import com.shop.ordering.domain.valueobject.id.PedidoId;
import com.shop.ordering.domain.valueobject.id.ProdutoId;
import org.junit.jupiter.api.Test;

class ItemPedidoTest {


    @Test
    public void deveGerar() {
        ItemPedido.novo()
                .produtoId(new ProdutoId())
                .id(new PedidoId())
                .nomeProduto(new NomeProduto(" Mouse "))
                .preco(new Dinheiro("100"))
                .quantidade(new Quantidade(1))
                .build();
    }
}
