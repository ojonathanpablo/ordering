package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.valueobject.id.ClienteId;
import org.junit.jupiter.api.Test;

class PedidoTest {

    @Test
    public void deveGerar(){
        Pedido pedido = Pedido.rascunho(new ClienteId());
    }

}
