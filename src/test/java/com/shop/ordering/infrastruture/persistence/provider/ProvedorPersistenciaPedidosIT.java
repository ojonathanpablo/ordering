package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.PedidoTestDataBuilder;
import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadePedido;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaPedido;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        ProvedorPersistenciaPedidos.class,
        MapeadorEntidadePedido.class,
        MapeadorDominioPedido.class,
        ConfigAuditoriaDadosSpring.class
})
class ProvedorPersistenciaPedidosIT {

    private final ProvedorPersistenciaPedidos provedorPersistenciaPedidos;
    private final RepositorioPersistenciaPedido repositorioPersistenciaPedido;

    @Autowired
    public ProvedorPersistenciaPedidosIT(ProvedorPersistenciaPedidos provedorPersistenciaPedidos,
                                          RepositorioPersistenciaPedido repositorioPersistenciaPedido) {
        this.provedorPersistenciaPedidos = provedorPersistenciaPedidos;
        this.repositorioPersistenciaPedido = repositorioPersistenciaPedido;
    }

    @Test
    public void deveAtualizarEManterEstadoDaEntidadePersistencia() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().status(StatusPedido.REALIZADO).build();
        long pedidoId = pedido.id().valor().toLong();
        provedorPersistenciaPedidos.adicionar(pedido);

        EntidadePersistenciaPedido entidadePersistencia = repositorioPersistenciaPedido.findById(pedidoId).orElseThrow();

        Assertions.assertThat(entidadePersistencia.getStatus()).isEqualTo(StatusPedido.REALIZADO.name());

        Assertions.assertThat(entidadePersistencia.getIdDoUsuarioQueCriou()).isNotNull();
        Assertions.assertThat(entidadePersistencia.getUltimaModificacao()).isNotNull();
        Assertions.assertThat(entidadePersistencia.getIdDoUsuarioDaUltimaModificacao()).isNotNull();

        pedido = provedorPersistenciaPedidos.porId(pedido.id()).orElseThrow();
        pedido.marcaPago();
        provedorPersistenciaPedidos.adicionar(pedido);

        entidadePersistencia = repositorioPersistenciaPedido.findById(pedidoId).orElseThrow();

        Assertions.assertThat(entidadePersistencia.getStatus()).isEqualTo(StatusPedido.PAGO.name());

        Assertions.assertThat(entidadePersistencia.getIdDoUsuarioQueCriou()).isNotNull();
        Assertions.assertThat(entidadePersistencia.getUltimaModificacao()).isNotNull();
        Assertions.assertThat(entidadePersistencia.getIdDoUsuarioDaUltimaModificacao()).isNotNull();
    }

    @Test
    public void deveVerificarSeExiste() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();

        Assertions.assertThat(provedorPersistenciaPedidos.existe(pedido.id())).isFalse();

        provedorPersistenciaPedidos.adicionar(pedido);

        Assertions.assertThat(provedorPersistenciaPedidos.existe(pedido.id())).isTrue();
    }

    @Test
    public void deveContar() {
        Assertions.assertThat(provedorPersistenciaPedidos.contar()).isZero();

        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().build());

        Assertions.assertThat(provedorPersistenciaPedidos.contar()).isEqualTo(1);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deveAdicionarEEncontrarSemFalharSemTransacao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();
        provedorPersistenciaPedidos.adicionar(pedido);

        Assertions.assertThatNoException().isThrownBy(
                () -> provedorPersistenciaPedidos.porId(pedido.id()).orElseThrow()
        );
    }

}
