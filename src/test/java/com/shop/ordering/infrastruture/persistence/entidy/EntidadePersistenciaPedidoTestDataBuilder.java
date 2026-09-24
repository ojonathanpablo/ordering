package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido.EntidadePersistenciaPedidoBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public class EntidadePersistenciaPedidoTestDataBuilder {

    private EntidadePersistenciaPedidoTestDataBuilder() {
    }

    public static EntidadePersistenciaPedidoBuilder pedidoExistente() {
        return EntidadePersistenciaPedido.builder()
                .id(GeradorId.gerarTSID().toLong())
                .clienteId(GeradorId.gerarUUIDBaseadoTempo())
                .quantidade(2)
                .valorTotal(new BigDecimal(1000))
                .status("RASCUNHO")
                .metodoPagamento("CARTAO_CREDITO")
                .realizadoEm(OffsetDateTime.now());
    }
}
