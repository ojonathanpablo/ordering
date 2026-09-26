package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaClienteTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedidoTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    private EntidadePersistenciaCliente cliente;

    @Autowired
    public RepositorioPersistenciaPedidoIT(RepositorioPersistenciaPedido repositorioPersistenciaPedido,
                                           RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.repositorioPersistenciaPedido = repositorioPersistenciaPedido;
        this.repositorioPersistenciaCliente = repositorioPersistenciaCliente;
    }

    @BeforeEach
    public void setUp() {
        cliente = repositorioPersistenciaCliente.saveAndFlush(
                EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build()
        );
    }

    @Test
    public void devePersistir() {
        EntidadePersistenciaPedido entidade = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente()
                .cliente(cliente)
                .build();

        repositorioPersistenciaPedido.saveAndFlush(entidade);
        Assertions.assertThat(repositorioPersistenciaPedido.existsById(entidade.getId())).isTrue();

        EntidadePersistenciaPedido entidadeSalva = repositorioPersistenciaPedido.findById(entidade.getId()).orElseThrow();

        Assertions.assertThat(entidadeSalva.getItems()).isNotEmpty();
    }

    @Test
    public void deveContar() {
        long contagemDePedidos = repositorioPersistenciaPedido.count();
        Assertions.assertThat(contagemDePedidos).isZero();
    }

    @Test
    public void deveDefinirValoresDeAuditoria() {
        EntidadePersistenciaPedido entidade = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente()
                .cliente(cliente)
                .build();
        entidade = repositorioPersistenciaPedido.saveAndFlush(entidade);

        Assertions.assertThat(entidade.getIdDoUsuarioQueCriou()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacao()).isNotNull();
        Assertions.assertThat(entidade.getIdDoUsuarioDaUltimaModificacao()).isNotNull();
    }

}