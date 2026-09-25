package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.Cliente;
import com.shop.ordering.domain.model.entity.ClienteTestDataBuilder;
import com.shop.ordering.domain.model.valueobject.Email;
import com.shop.ordering.domain.model.valueobject.PontosFidelidade;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioCliente;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadeCliente;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@DataJpaTest
@Import({
        ProvedorPersistenciaClientes.class,
        MapeadorEntidadeCliente.class,
        MapeadorDominioCliente.class,
        ConfigAuditoriaDadosSpring.class
})
class ProvedorPersistenciaClientesIT {

    private final ProvedorPersistenciaClientes provedorPersistenciaClientes;
    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    @Autowired
    public ProvedorPersistenciaClientesIT(ProvedorPersistenciaClientes provedorPersistenciaClientes,
                                          RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.provedorPersistenciaClientes = provedorPersistenciaClientes;
        this.repositorioPersistenciaCliente = repositorioPersistenciaCliente;
    }

    @Test
    public void devePersistirTodosOsDadosDoCliente() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        UUID clienteId = cliente.id().valor();

        provedorPersistenciaClientes.adicionar(cliente);

        EntidadePersistenciaCliente entidade = repositorioPersistenciaCliente.findById(clienteId).orElseThrow();

        Assertions.assertThat(entidade.getNomeCompleto().getPrimeiroNome()).isEqualTo(cliente.nomeCompleto().primeiroNome());
        Assertions.assertThat(entidade.getNomeCompleto().getUltimoNome()).isEqualTo(cliente.nomeCompleto().ultimoNome());
        Assertions.assertThat(entidade.getDataNascimento()).isEqualTo(cliente.dataNascimento().valor());
        Assertions.assertThat(entidade.getEmail()).isEqualTo(cliente.email().valor());
        Assertions.assertThat(entidade.getTelefone()).isEqualTo(cliente.telefone().valor());
        Assertions.assertThat(entidade.getDocumento()).isEqualTo(cliente.documento().valor());
        Assertions.assertThat(entidade.getNotificacoesPromocionaisPermitidas()).isEqualTo(cliente.isNotificacoesPromocionaisPermitidas());
        Assertions.assertThat(entidade.getArquivado()).isEqualTo(cliente.isArquivado());
        Assertions.assertThat(entidade.getPontosFidelidade()).isEqualTo(cliente.pontosFidelidade().valor());
        Assertions.assertThat(entidade.getEndereco().getRua()).isEqualTo(cliente.endereco().rua());
        Assertions.assertThat(entidade.getEndereco().getCep()).isEqualTo(cliente.endereco().cep().valor());

        Assertions.assertThat(entidade.getCriadoPorIDdoUsuario()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoEm()).isNotNull();
        Assertions.assertThat(entidade.getUltimaModificacaoPorIDdoUsuario()).isNotNull();
    }

    @Test
    public void deveAtualizarVersaoNoDominioAposPersistir() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        Assertions.assertThat(cliente.versao()).isNull();

        provedorPersistenciaClientes.adicionar(cliente);

        Assertions.assertThat(cliente.versao()).isZero();
    }

    @Test
    public void deveIncrementarVersaoAoAtualizar() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        provedorPersistenciaClientes.adicionar(cliente);

        cliente = provedorPersistenciaClientes.porId(cliente.id()).orElseThrow();
        cliente.alterarEmail(new Email("novo@email.com"));
        provedorPersistenciaClientes.adicionar(cliente);

        Assertions.assertThat(cliente.versao()).isEqualTo(1L);

        EntidadePersistenciaCliente entidade = repositorioPersistenciaCliente.findById(cliente.id().valor()).orElseThrow();
        Assertions.assertThat(entidade.getEmail()).isEqualTo("novo@email.com");
        Assertions.assertThat(entidade.getVersao()).isEqualTo(1L);
    }

    @Test
    public void deveLancarExcecaoAoAtualizarComVersaoAntiga() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        provedorPersistenciaClientes.adicionar(cliente);

        Cliente clienteT1 = provedorPersistenciaClientes.porId(cliente.id()).orElseThrow();
        Cliente clienteT2 = provedorPersistenciaClientes.porId(cliente.id()).orElseThrow();

        clienteT1.adicionarPontosFidelidade(new PontosFidelidade(10));
        provedorPersistenciaClientes.adicionar(clienteT1);

        clienteT2.alterarEmail(new Email("outro@email.com"));

        Assertions.assertThatExceptionOfType(ObjectOptimisticLockingFailureException.class)
                .isThrownBy(() -> provedorPersistenciaClientes.adicionar(clienteT2));
    }

    @Test
    public void deveBuscarPorIdComMapeamentoCompleto() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        provedorPersistenciaClientes.adicionar(cliente);

        Cliente clienteEncontrado = provedorPersistenciaClientes.porId(cliente.id()).orElseThrow();

        Assertions.assertThat(clienteEncontrado)
                .usingRecursiveComparison()
                .ignoringFields("registradoEm", "versao")
                .isEqualTo(cliente);
        Assertions.assertThat(clienteEncontrado.versao()).isEqualTo(cliente.versao());
    }

    @Test
    public void deveVerificarSeExiste() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();

        Assertions.assertThat(provedorPersistenciaClientes.existe(cliente.id())).isFalse();

        provedorPersistenciaClientes.adicionar(cliente);

        Assertions.assertThat(provedorPersistenciaClientes.existe(cliente.id())).isTrue();
    }

    @Test
    public void deveContar() {
        Assertions.assertThat(provedorPersistenciaClientes.contar()).isZero();

        provedorPersistenciaClientes.adicionar(ClienteTestDataBuilder.clienteNovo().build());
        provedorPersistenciaClientes.adicionar(ClienteTestDataBuilder.clienteNovo().build());

        Assertions.assertThat(provedorPersistenciaClientes.contar()).isEqualTo(2);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deveAdicionarEEncontrarSemFalharSemTransacao() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();
        provedorPersistenciaClientes.adicionar(cliente);

        Assertions.assertThatNoException().isThrownBy(
                () -> provedorPersistenciaClientes.porId(cliente.id()).orElseThrow()
        );
    }

}
