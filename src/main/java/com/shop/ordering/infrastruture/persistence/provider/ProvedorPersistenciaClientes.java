package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.Cliente;
import com.shop.ordering.domain.model.repository.Clientes;
import com.shop.ordering.domain.model.valueobject.Email;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioCliente;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadeCliente;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvedorPersistenciaClientes implements Clientes {

    private final RepositorioPersistenciaCliente persistenciaCliente;
    private final MapeadorDominioCliente mapeadorDominioCliente;
    private final MapeadorEntidadeCliente mapeadorEntidadeCliente;
    private final EntityManager entityManager;


    @Override
    public Optional<Cliente> porId(ClienteId clienteId) {
        Optional<EntidadePersistenciaCliente> cliente = persistenciaCliente.findById(clienteId.valor());
        return cliente.map(mapeadorDominioCliente::paraDominio);
    }

    @Override
    public boolean existe(ClienteId clienteId) {
        return persistenciaCliente.existsById(clienteId.valor());
    }

    @Override
    @Transactional(readOnly = false)
    public void adicionar(Cliente raizDeAgregado) {
        UUID clienteId = raizDeAgregado.id().valor();

        persistenciaCliente.findById(clienteId)
                .ifPresentOrElse(
                        (entidadePersistencia) ->
                                atualizar(raizDeAgregado, entidadePersistencia),
                        () -> inserir(raizDeAgregado));

    }

    @Override
    public int contar() {
        return (int) persistenciaCliente.count();
    }

    @Override
    public Optional<Cliente> deEmail(Email email) {
        return persistenciaCliente.findByEmail(email.valor())
                .map(mapeadorDominioCliente::paraDominio);
    }

    @Override
    public boolean eEmailUnico(Email email, ClienteId exceptCustomerId) {
        return !persistenciaCliente.existsByEmailAndIdNot(email.valor(), exceptCustomerId.valor());
    }

    private void atualizar(Cliente raizDeAgregado, EntidadePersistenciaCliente entidadePersistenciaCliente) {
        entidadePersistenciaCliente = mapeadorEntidadeCliente.mesclar(entidadePersistenciaCliente, raizDeAgregado);
        entityManager.detach(entidadePersistenciaCliente);
        entidadePersistenciaCliente = persistenciaCliente.saveAndFlush(entidadePersistenciaCliente);
        updateVersion(raizDeAgregado, entidadePersistenciaCliente);
    }

    private void inserir(Cliente raizDeAgregado) {
        EntidadePersistenciaCliente entidadePersistenciaCliente = mapeadorEntidadeCliente.paraEntidade(raizDeAgregado);
        entidadePersistenciaCliente = persistenciaCliente.saveAndFlush(entidadePersistenciaCliente);
        updateVersion(raizDeAgregado, entidadePersistenciaCliente);
    }

    @SneakyThrows
    private void updateVersion(Cliente aggregateRoot, EntidadePersistenciaCliente persistenceEntity) {
        Field versao = aggregateRoot.getClass().getDeclaredField("versao");
        versao.setAccessible(true);
        ReflectionUtils.setField(versao, aggregateRoot, persistenceEntity.getVersao());
        versao.setAccessible(false);
    }

}
