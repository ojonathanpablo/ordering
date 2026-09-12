package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.id.ClienteId;
import org.junit.jupiter.api.Test;

class PedidoTest {

    @Test
    public void deveGerar(){
        Pedido pedido = Pedido.rascunho(new ClienteId());
    }

}
