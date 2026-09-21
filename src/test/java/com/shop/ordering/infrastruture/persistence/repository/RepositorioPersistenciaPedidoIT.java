package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@SpringBootTest
@Transactional
class RepositorioPersistenciaPedidoIT {

    private final RepositorioPersistenciaPedido repositorioPersistenciaPedido;

    @Autowired
    public RepositorioPersistenciaPedidoIT(RepositorioPersistenciaPedido repositorioPersistenciaPedido) {
        this.repositorioPersistenciaPedido = repositorioPersistenciaPedido;
    }

    @Test
    public void shouldPersist() {
        long orderId = GeradorId.gerarTSID().toLong();
        EntidadePersistenciaPedido entidade =  EntidadePersistenciaPedido.builder()
                .id(orderId)
                .clienteId(GeradorId.gerarUUIDBaseadoTempo())
                .quantidade(2)
                .valorTotal(new BigDecimal(100))
                .status("RASCUNHO")
                .metodoPagamento("CARTAO_CREDITO")
                .pagoEm(OffsetDateTime.now())
                .build();

        repositorioPersistenciaPedido.saveAndFlush(entidade);
        Assertions.assertThat(repositorioPersistenciaPedido.existsById(orderId)).isTrue();
    }

    @Test
    public void deveContar() {
        long contagemDePedidos = repositorioPersistenciaPedido.count();
        Assertions.assertThat(contagemDePedidos).isZero();
    }

}