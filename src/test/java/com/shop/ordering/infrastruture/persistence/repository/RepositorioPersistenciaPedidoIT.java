package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedidoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ConfigAuditoriaDadosSpring.class)
class RepositorioPersistenciaPedidoIT {

    private final RepositorioPersistenciaPedido repositorioPersistenciaPedido;

    @Autowired
    public RepositorioPersistenciaPedidoIT(RepositorioPersistenciaPedido repositorioPersistenciaPedido) {
        this.repositorioPersistenciaPedido = repositorioPersistenciaPedido;
    }

    @Test
    public void devePersistir() {
        EntidadePersistenciaPedido entidade = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente().build();

        repositorioPersistenciaPedido.saveAndFlush(entidade);
        Assertions.assertThat(repositorioPersistenciaPedido.existsById(entidade.getId())).isTrue();
    }

    @Test
    public void deveContar() {
        long contagemDePedidos = repositorioPersistenciaPedido.count();
        Assertions.assertThat(contagemDePedidos).isZero();
    }

    @Test
    public void deveDefinirValoresDeAuditoria() {
        EntidadePersistenciaPedido entidade = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente().build();
        entidade = repositorioPersistenciaPedido.saveAndFlush(entidade);

        Assertions.assertThat(entidade.getIdDoUsuarioQueCriou()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacao()).isNotNull();
        Assertions.assertThat(entidade.getIdDoUsuarioDaUltimaModificacao()).isNotNull();
    }

}