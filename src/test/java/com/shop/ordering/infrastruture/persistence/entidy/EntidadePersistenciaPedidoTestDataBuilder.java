package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido.EntidadePersistenciaPedidoBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.Set;

public class EntidadePersistenciaPedidoTestDataBuilder {

    private EntidadePersistenciaPedidoTestDataBuilder() {
    }

    public static EntidadePersistenciaPedidoBuilder pedidoExistente() {
        return EntidadePersistenciaPedido.builder()
                .id(GeradorId.gerarTSID().toLong())
                .clienteId(GeradorId.gerarUUIDBaseadoTempo())
                .quantidade(3)
                .valorTotal(new BigDecimal(1250))
                .status("RASCUNHO")
                .metodoPagamento("CARTAO_CREDITO")
                .realizadoEm(OffsetDateTime.now())
                .items(Set.of(
                        itemExistente().build(),
                        itemExistenteAlt().build()
                ));
    }

    public static EntidadePersistenciaItemPedido.EntidadePersistenciaItemPedidoBuilder itemExistente() {
        return EntidadePersistenciaItemPedido.builder()
                .id(GeradorId.gerarTSID().toLong())
                .preco(new BigDecimal(500))
                .quantidade(2)
                .valorTotal(new BigDecimal(1000))
                .produtoNome("Notebook")
                .produtoId(GeradorId.gerarUUIDBaseadoTempo());
    }

    public static EntidadePersistenciaItemPedido.EntidadePersistenciaItemPedidoBuilder itemExistenteAlt() {
        return EntidadePersistenciaItemPedido.builder()
                .id(GeradorId.gerarTSID().toLong())
                .preco(new BigDecimal(250))
                .quantidade(1)
                .valorTotal(new BigDecimal(250))
                .produtoNome("Mouse pad")
                .produtoId(GeradorId.gerarUUIDBaseadoTempo());
    }
}
