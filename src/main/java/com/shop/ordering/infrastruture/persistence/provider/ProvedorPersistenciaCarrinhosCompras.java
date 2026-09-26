package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.CarrinhoCompras;
import com.shop.ordering.domain.model.repository.CarrinhosCompras;
import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadeCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCarrinhoCompras;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvedorPersistenciaCarrinhosCompras implements CarrinhosCompras {

    private final RepositorioPersistenciaCarrinhoCompras repositorioPersistenciaCarrinhoCompras;
    private final MapeadorEntidadeCarrinhoCompras mapeadorEntidadeCarrinhoCompras;
    private final MapeadorDominioCarrinhoCompras mapeadorDominioCarrinhoCompras;
    private final EntityManager entityManager;

    @Override
    public Optional<CarrinhoCompras> porId(CarrinhoComprasId carrinhoComprasId) {
        return repositorioPersistenciaCarrinhoCompras.findById(carrinhoComprasId.valor().toLong())
                .map(mapeadorDominioCarrinhoCompras::paraDominio);
    }

    @Override
    public Optional<CarrinhoCompras> doCliente(ClienteId clienteId) {
        return repositorioPersistenciaCarrinhoCompras.findByCliente_Id(clienteId.valor())
                .map(mapeadorDominioCarrinhoCompras::paraDominio);
    }

    @Override
    public boolean existe(CarrinhoComprasId carrinhoComprasId) {
        return repositorioPersistenciaCarrinhoCompras.existsById(carrinhoComprasId.valor().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void adicionar(CarrinhoCompras raizDeAgregado) {
        long carrinhoComprasId = raizDeAgregado.id().valor().toLong();

        repositorioPersistenciaCarrinhoCompras.findById(carrinhoComprasId)
                .ifPresentOrElse(
                        (entidadePersistencia) ->
                                atualizar(raizDeAgregado, entidadePersistencia),
                        () -> inserir(raizDeAgregado)
                );
    }

    @Override
    @Transactional(readOnly = false)
    public void remover(CarrinhoCompras raizDeAgregado) {
        remove(raizDeAgregado.id());
    }

    @Override
    @Transactional(readOnly = false)
    public void remove(CarrinhoComprasId carrinhoComprasId) {
        repositorioPersistenciaCarrinhoCompras.deleteById(carrinhoComprasId.valor().toLong());
    }

    @Override
    public int contar() {
        return (int) repositorioPersistenciaCarrinhoCompras.count();
    }

    private void atualizar(CarrinhoCompras raizDeAgregado, EntidadePersistenciaCarrinhoCompras entidadePersistencia) {
        entidadePersistencia = mapeadorEntidadeCarrinhoCompras.mesclar(entidadePersistencia, raizDeAgregado);
        entityManager.detach(entidadePersistencia);
        entidadePersistencia = repositorioPersistenciaCarrinhoCompras.saveAndFlush(entidadePersistencia);
        updateVersion(raizDeAgregado, entidadePersistencia);
    }

    private void inserir(CarrinhoCompras raizDeAgregado) {
        EntidadePersistenciaCarrinhoCompras entidadePersistencia = mapeadorEntidadeCarrinhoCompras.paraEntidade(raizDeAgregado);
        entidadePersistencia = repositorioPersistenciaCarrinhoCompras.saveAndFlush(entidadePersistencia);
        updateVersion(raizDeAgregado, entidadePersistencia);
    }

    @SneakyThrows
    private void updateVersion(CarrinhoCompras aggregateRoot, EntidadePersistenciaCarrinhoCompras persistenceEntity) {
        Field versao = aggregateRoot.getClass().getDeclaredField("versao");
        versao.setAccessible(true);
        ReflectionUtils.setField(versao, aggregateRoot, persistenceEntity.getVersao());
        versao.setAccessible(false);
    }
}
