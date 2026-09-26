package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.repository.Pedidos;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadePedido;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaPedido;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.Year;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProvedorPersistenciaPedidos implements Pedidos {

    private final RepositorioPersistenciaPedido repositorioPersistenciaPedido;
    private final MapeadorEntidadePedido mapeadorEntidadePedido;
    private final MapeadorDominioPedido mapeadorDominioPedido;
    private final EntityManager entityManager;

    @Override
    public Optional<Pedido> porId(PedidoId pedidoId) {
        Optional<EntidadePersistenciaPedido> pedido = repositorioPersistenciaPedido.findById(pedidoId.valor().toLong());
        return pedido.map(mapeadorDominioPedido::paraDominio);
    }

    @Override
    public boolean existe(PedidoId pedidoId) {
        return repositorioPersistenciaPedido.existsById(pedidoId.valor().toLong());
    }

    @Override
    @Transactional(readOnly = false)
    public void adicionar(Pedido raizDeAgregado) {
        long pedidoId = raizDeAgregado.id().valor().toLong();

        repositorioPersistenciaPedido.findById(pedidoId)
                .ifPresentOrElse(
                        (entidadePersistencia) ->
                                atualizar(raizDeAgregado, entidadePersistencia),
                        () -> inserir(raizDeAgregado)
                );
    }

    private void atualizar(Pedido raizDeAgregado, EntidadePersistenciaPedido entidadePersistencia) {
        entidadePersistencia = mapeadorEntidadePedido.mesclar(entidadePersistencia, raizDeAgregado);
        entityManager.detach(entidadePersistencia);
        entidadePersistencia = repositorioPersistenciaPedido.saveAndFlush(entidadePersistencia);
        updateVersion(raizDeAgregado, entidadePersistencia);
    }

    private void inserir(Pedido raizDeAgregado) {
        EntidadePersistenciaPedido entidadePersistencia = mapeadorEntidadePedido.paraEntidade(raizDeAgregado);
        entidadePersistencia = repositorioPersistenciaPedido.saveAndFlush(entidadePersistencia);
        updateVersion(raizDeAgregado, entidadePersistencia);
    }

    @SneakyThrows
    private void updateVersion(Pedido aggregateRoot, EntidadePersistenciaPedido persistenceEntity) {
        Field versao = aggregateRoot.getClass().getDeclaredField("versao");
        versao.setAccessible(true);
        ReflectionUtils.setField(versao, aggregateRoot, persistenceEntity.getVersion());
        versao.setAccessible(false);
    }

    @Override
    public int contar() {
        return (int) repositorioPersistenciaPedido.count();
    }

    @Override
    public List<Pedido> realizadosPorClienteNoAno(ClienteId clienteId, Year ano) {
        List<EntidadePersistenciaPedido> entidades = repositorioPersistenciaPedido.realizadosPorClienteNoAno(
                clienteId.valor(),
                ano.getValue()
        );

        return entidades.stream().map(mapeadorDominioPedido::paraDominio).collect(Collectors.toList());
    }

    @Override
    public long quantidadeVendasPorClienteNoAno(ClienteId clienteId, Year ano) {
        return repositorioPersistenciaPedido.quantidadeVendasPorClienteNoAno(clienteId.valor(), ano.getValue());
    }

    @Override
    public Dinheiro totalVendidoParaCliente(ClienteId clienteId) {
        return new Dinheiro(repositorioPersistenciaPedido.totalVendidoParaCliente(clienteId.valor()));
    }
}
