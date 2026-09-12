package com.algashop.ordering.domain.entity;


import com.algashop.ordering.domain.valueobject.Dinheiro;
import com.algashop.ordering.domain.valueobject.NomeProduto;
import com.algashop.ordering.domain.valueobject.Quantidade;
import com.algashop.ordering.domain.valueobject.id.PedidoId;
import com.algashop.ordering.domain.valueobject.id.ProdutoId;
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
