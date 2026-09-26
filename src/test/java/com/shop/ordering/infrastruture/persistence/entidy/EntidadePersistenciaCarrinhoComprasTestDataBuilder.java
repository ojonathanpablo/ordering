package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras.EntidadePersistenciaCarrinhoComprasBuilder;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class EntidadePersistenciaCarrinhoComprasTestDataBuilder {

    private EntidadePersistenciaCarrinhoComprasTestDataBuilder() {
    }

    public static EntidadePersistenciaCarrinhoComprasBuilder carrinhoExistente() {
        return EntidadePersistenciaCarrinhoCompras.builder()
                .id(GeradorId.gerarTSID().toLong())
                .cliente(EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build())
                .valorTotal(new BigDecimal(1250))
                .totalDeItens(3)
                .criadoEm(OffsetDateTime.now())
                .itens(new HashSet<>(Set.of(
                        itemExistente().build(),
                        itemExistenteAlt().build()
                )));
    }

    public static EntidadePersistenciaItemCarrinhoCompras.EntidadePersistenciaItemCarrinhoComprasBuilder itemExistente() {
        return EntidadePersistenciaItemCarrinhoCompras.builder()
                .id(GeradorId.gerarTSID().toLong())
                .produtoId(UUID.randomUUID())
                .nomeProduto("Notebook")
                .preco(new BigDecimal(500))
                .quantidade(2)
                .valorTotal(new BigDecimal(1000))
                .disponivel(true);
    }

    public static EntidadePersistenciaItemCarrinhoCompras.EntidadePersistenciaItemCarrinhoComprasBuilder itemExistenteAlt() {
        return EntidadePersistenciaItemCarrinhoCompras.builder()
                .id(GeradorId.gerarTSID().toLong())
                .produtoId(UUID.randomUUID())
                .nomeProduto("Memória RAM")
                .preco(new BigDecimal(250))
                .quantidade(1)
                .valorTotal(new BigDecimal(250))
                .disponivel(true);
    }
}
