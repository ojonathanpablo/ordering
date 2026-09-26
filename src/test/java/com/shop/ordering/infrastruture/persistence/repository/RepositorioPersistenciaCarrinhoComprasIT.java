package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoComprasTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaClienteTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ConfigAuditoriaDadosSpring.class)
class RepositorioPersistenciaCarrinhoComprasIT {

    private final RepositorioPersistenciaCarrinhoCompras repositorioPersistenciaCarrinhoCompras;
    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    private EntidadePersistenciaCliente cliente;

    @Autowired
    public RepositorioPersistenciaCarrinhoComprasIT(RepositorioPersistenciaCarrinhoCompras repositorioPersistenciaCarrinhoCompras,
                                                    RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.repositorioPersistenciaCarrinhoCompras = repositorioPersistenciaCarrinhoCompras;
        this.repositorioPersistenciaCliente = repositorioPersistenciaCliente;
    }

    @BeforeEach
    public void setUp() {
        cliente = repositorioPersistenciaCliente.saveAndFlush(
                EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build()
        );
    }

    @Test
    public void devePersistirCarrinhoComItens() {
        EntidadePersistenciaCarrinhoCompras entidade = EntidadePersistenciaCarrinhoComprasTestDataBuilder.carrinhoExistente()
                .cliente(cliente)
                .build();

        repositorioPersistenciaCarrinhoCompras.saveAndFlush(entidade);
        Assertions.assertThat(repositorioPersistenciaCarrinhoCompras.existsById(entidade.getId())).isTrue();

        EntidadePersistenciaCarrinhoCompras entidadeSalva = repositorioPersistenciaCarrinhoCompras.findById(entidade.getId()).orElseThrow();

        Assertions.assertThat(entidadeSalva.getItens()).hasSize(2);
        Assertions.assertThat(entidadeSalva.getItens())
                .allSatisfy(item -> Assertions.assertThat(item.getCarrinhoComprasId()).isEqualTo(entidade.getId()));
    }

    @Test
    public void deveContar() {
        Assertions.assertThat(repositorioPersistenciaCarrinhoCompras.count()).isZero();

        repositorioPersistenciaCarrinhoCompras.saveAndFlush(
                EntidadePersistenciaCarrinhoComprasTestDataBuilder.carrinhoExistente().cliente(cliente).build()
        );

        Assertions.assertThat(repositorioPersistenciaCarrinhoCompras.count()).isEqualTo(1L);
    }

    @Test
    public void deveDefinirValoresDeAuditoria() {
        EntidadePersistenciaCarrinhoCompras entidade = EntidadePersistenciaCarrinhoComprasTestDataBuilder.carrinhoExistente()
                .cliente(cliente)
                .build();
        entidade = repositorioPersistenciaCarrinhoCompras.saveAndFlush(entidade);

        Assertions.assertThat(entidade.getCriadoPorIdDoUsuario()).isNotNull();
        Assertions.assertThat(entidade.getCriadoEm()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoPorIdDoUsuario()).isNotNull();

        Assertions.assertThat(entidade.getItens()).allSatisfy(item -> {
            Assertions.assertThat(item.getCriadoPorIdDoUsuario()).isNotNull();
            Assertions.assertThat(item.getCriadoEm()).isNotNull();
            Assertions.assertThat(item.getUltimaModificacaoEm()).isNotNull();
            Assertions.assertThat(item.getUltimaModificacaoPorIdDoUsuario()).isNotNull();
        });
    }

    @Test
    public void deveBuscarPorClienteId() {
        EntidadePersistenciaCarrinhoCompras entidade = EntidadePersistenciaCarrinhoComprasTestDataBuilder.carrinhoExistente()
                .cliente(cliente)
                .build();
        repositorioPersistenciaCarrinhoCompras.saveAndFlush(entidade);

        EntidadePersistenciaCarrinhoCompras encontrado = repositorioPersistenciaCarrinhoCompras.findByCliente_Id(cliente.getId()).orElseThrow();

        Assertions.assertThat(encontrado.getId()).isEqualTo(entidade.getId());
        Assertions.assertThat(encontrado.getItens()).hasSize(2);
    }

}
