package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaClienteTestDataBuilder;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import(ConfigAuditoriaDadosSpring.class)
class RepositorioPersistenciaClienteIT {

    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    @Autowired
    public RepositorioPersistenciaClienteIT(RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.repositorioPersistenciaCliente = repositorioPersistenciaCliente;
    }

    @Test
    public void devePersistir() {
        EntidadePersistenciaCliente entidade = EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build();

        repositorioPersistenciaCliente.saveAndFlush(entidade);
        Assertions.assertThat(repositorioPersistenciaCliente.existsById(entidade.getId())).isTrue();

        EntidadePersistenciaCliente entidadeSalva = repositorioPersistenciaCliente.findById(entidade.getId()).orElseThrow();

        Assertions.assertThat(entidadeSalva.getNomeCompleto().getPrimeiroNome()).isEqualTo("John");
        Assertions.assertThat(entidadeSalva.getEndereco().getRua()).isEqualTo("Bourbon Street");
        Assertions.assertThat(entidadeSalva.getVersao()).isZero();
    }

    @Test
    public void deveContar() {
        long contagemDeClientes = repositorioPersistenciaCliente.count();
        Assertions.assertThat(contagemDeClientes).isZero();
    }

    @Test
    public void deveDefinirValoresDeAuditoria() {
        EntidadePersistenciaCliente entidade = EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build();
        entidade = repositorioPersistenciaCliente.saveAndFlush(entidade);

        Assertions.assertThat(entidade.getCriadoPorIDdoUsuario()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoPorIDdoUsuario()).isNotNull();
    }

}
