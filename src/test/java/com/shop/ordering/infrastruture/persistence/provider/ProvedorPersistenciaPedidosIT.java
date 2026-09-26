package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.PedidoTestDataBuilder;
import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaClienteTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioPedido;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadePedido;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaPedido;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.Year;
import java.util.List;

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
    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    private ClienteId clienteId;

    @Autowired
    public ProvedorPersistenciaPedidosIT(ProvedorPersistenciaPedidos provedorPersistenciaPedidos,
                                          RepositorioPersistenciaPedido repositorioPersistenciaPedido,
                                          RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.provedorPersistenciaPedidos = provedorPersistenciaPedidos;
        this.repositorioPersistenciaPedido = repositorioPersistenciaPedido;
        this.repositorioPersistenciaCliente = repositorioPersistenciaCliente;
    }

    @BeforeEach
    public void setUp() {
        var cliente = repositorioPersistenciaCliente.saveAndFlush(
                EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build()
        );
        clienteId = new ClienteId(cliente.getId());
    }

    @Test
    public void deveAtualizarEManterEstadoDaEntidadePersistencia() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.REALIZADO).build();
        long pedidoId = pedido.id().valor().toLong();
        provedorPersistenciaPedidos.adicionar(pedido);

        EntidadePersistenciaPedido entidadePersistencia = repositorioPersistenciaPedido.findById(pedidoId).orElseThrow();

        Assertions.assertThat(entidadePersistencia.getStatus()).isEqualTo(StatusPedido.REALIZADO.name());
        Assertions.assertThat(entidadePersistencia.getClienteId()).isEqualTo(clienteId.valor());

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
        Pedido pedido = PedidoTestDataBuilder.umPedido().clienteId(clienteId).build();

        Assertions.assertThat(provedorPersistenciaPedidos.existe(pedido.id())).isFalse();

        provedorPersistenciaPedidos.adicionar(pedido);

        Assertions.assertThat(provedorPersistenciaPedidos.existe(pedido.id())).isTrue();
    }

    @Test
    public void deveContar() {
        Assertions.assertThat(provedorPersistenciaPedidos.contar()).isZero();

        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().clienteId(clienteId).build());

        Assertions.assertThat(provedorPersistenciaPedidos.contar()).isEqualTo(1);
    }

    @Test
    public void deveListarPedidosRealizadosPeloClienteNoAno() {
        Pedido realizado = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.REALIZADO).build();
        Pedido pago = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.PAGO).build();
        Pedido rascunho = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.RASCUNHO).build();
        Pedido deOutroCliente = PedidoTestDataBuilder.umPedido().clienteId(outroClienteSalvo()).status(StatusPedido.REALIZADO).build();

        provedorPersistenciaPedidos.adicionar(realizado);
        provedorPersistenciaPedidos.adicionar(pago);
        provedorPersistenciaPedidos.adicionar(rascunho);
        provedorPersistenciaPedidos.adicionar(deOutroCliente);

        List<Pedido> pedidos = provedorPersistenciaPedidos.realizadosPorClienteNoAno(clienteId, Year.now());

        Assertions.assertThat(pedidos)
                .extracting(Pedido::id)
                .containsExactlyInAnyOrder(realizado.id(), pago.id());

        Assertions.assertThat(provedorPersistenciaPedidos.realizadosPorClienteNoAno(clienteId, Year.now().minusYears(1)))
                .isEmpty();
    }

    @Test
    public void deveContarVendasDoClienteNoAno() {
        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.PAGO).build());
        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.PRONTO).build());
        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.REALIZADO).build());
        provedorPersistenciaPedidos.adicionar(PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.CANCELADO).build());

        Assertions.assertThat(provedorPersistenciaPedidos.quantidadeVendasPorClienteNoAno(clienteId, Year.now()))
                .isEqualTo(2L);
        Assertions.assertThat(provedorPersistenciaPedidos.quantidadeVendasPorClienteNoAno(clienteId, Year.now().minusYears(1)))
                .isZero();
    }

    @Test
    public void deveSomarTotalVendidoParaCliente() {
        Pedido pago = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.PAGO).build();
        Pedido pronto = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.PRONTO).build();
        Pedido realizado = PedidoTestDataBuilder.umPedido().clienteId(clienteId).status(StatusPedido.REALIZADO).build();

        provedorPersistenciaPedidos.adicionar(pago);
        provedorPersistenciaPedidos.adicionar(pronto);
        provedorPersistenciaPedidos.adicionar(realizado);

        Dinheiro esperado = pago.valorTotal().somar(pronto.valorTotal());

        Assertions.assertThat(provedorPersistenciaPedidos.totalVendidoParaCliente(clienteId))
                .isEqualByComparingTo(esperado);
    }

    @Test
    public void deveRetornarZeroQuandoClienteNaoTemVendas() {
        Assertions.assertThat(provedorPersistenciaPedidos.totalVendidoParaCliente(clienteId))
                .isEqualByComparingTo(Dinheiro.ZERO);
    }

    private ClienteId outroClienteSalvo() {
        var outroCliente = repositorioPersistenciaCliente.saveAndFlush(
                EntidadePersistenciaClienteTestDataBuilder.clienteExistente().build()
        );
        return new ClienteId(outroCliente.getId());
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deveAdicionarEEncontrarSemFalharSemTransacao() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().clienteId(clienteId).build();
        provedorPersistenciaPedidos.adicionar(pedido);

        Assertions.assertThatNoException().isThrownBy(
                () -> provedorPersistenciaPedidos.porId(pedido.id()).orElseThrow()
        );
    }

}
