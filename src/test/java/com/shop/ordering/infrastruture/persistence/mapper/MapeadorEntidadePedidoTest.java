package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.ItemPedido;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.PedidoTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaItemPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedidoTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
class MapeadorEntidadePedidoTest {

    @Mock
    private RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    @InjectMocks
    private MapeadorEntidadePedido mapeadorEntidadePedido;

    @BeforeEach
    void setUp() {
        Mockito.when(repositorioPersistenciaCliente.getReferenceById(Mockito.any(UUID.class)))
                .thenAnswer(invocacao -> EntidadePersistenciaCliente.builder()
                        .id(invocacao.getArgument(0))
                        .build());
    }

    @Test
    void deveConverterDoDominioParaEntidade() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();

        EntidadePersistenciaPedido entidadePersistenciaPedido = mapeadorEntidadePedido.paraEntidade(pedido);

        Assertions.assertThat(entidadePersistenciaPedido).satisfies(
                p -> Assertions.assertThat(p.getId()).isEqualTo(pedido.id().valor().toLong()),
                p -> Assertions.assertThat(p.getClienteId()).isEqualTo(pedido.clienteId().valor()),
                p -> Assertions.assertThat(p.getValorTotal()).isEqualTo(pedido.valorTotal().valor()),
                p -> Assertions.assertThat(p.getQuantidade()).isEqualTo(pedido.quantidade().valor()),
                p -> Assertions.assertThat(p.getStatus()).isEqualTo(pedido.statusPedido().name()),
                p -> Assertions.assertThat(p.getMetodoPagamento()).isEqualTo(pedido.metodoPagamento().name()),
                p -> Assertions.assertThat(p.getRealizadoEm()).isEqualTo(pedido.realizadoEm()),
                p -> Assertions.assertThat(p.getPagoEm()).isEqualTo(pedido.pagoEm()),
                p -> Assertions.assertThat(p.getCanceladoEm()).isEqualTo(pedido.canceladoEm()),
                p -> Assertions.assertThat(p.getProntoEm()).isEqualTo(pedido.prontoEm())
        );
    }

    @Test
    void dadoPedidoSemItens_deveRemoverItensDaEntidadePersistencia() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().comItens(false).build();
        EntidadePersistenciaPedido entidadePersistenciaPedido = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente().build();

        Assertions.assertThat(pedido.itensPedido()).isEmpty();
        Assertions.assertThat(entidadePersistenciaPedido.getItems()).isNotEmpty();

        mapeadorEntidadePedido.mesclar(entidadePersistenciaPedido, pedido);

        Assertions.assertThat(entidadePersistenciaPedido.getItems()).isEmpty();
    }

    @Test
    void dadoPedidoComItens_deveAdicionarNaEntidadePersistencia() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().comItens(true).build();
        EntidadePersistenciaPedido entidadePersistenciaPedido = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente()
                .items(new HashSet<>())
                .build();

        Assertions.assertThat(pedido.itensPedido()).isNotEmpty();
        Assertions.assertThat(entidadePersistenciaPedido.getItems()).isEmpty();

        mapeadorEntidadePedido.mesclar(entidadePersistenciaPedido, pedido);

        Assertions.assertThat(entidadePersistenciaPedido.getItems()).isNotEmpty();
        Assertions.assertThat(entidadePersistenciaPedido.getItems().size()).isEqualTo(pedido.itensPedido().size());
    }

    @Test
    void dadoPedidoComItens_quandoMesclarAposRemoverItem_deveMesclarCorretamente() {
        Pedido pedido = PedidoTestDataBuilder.umPedido().build();

        Assertions.assertThat(pedido.itensPedido().size()).isEqualTo(2);

        Set<EntidadePersistenciaItemPedido> itensPersistencia = pedido.itensPedido().stream()
                .map(mapeadorEntidadePedido::paraEntidade)
                .collect(Collectors.toSet());

        EntidadePersistenciaPedido entidadePersistenciaPedido = EntidadePersistenciaPedidoTestDataBuilder.pedidoExistente()
                .items(itensPersistencia)
                .build();

        ItemPedido itemPedido = pedido.itensPedido().iterator().next();
        pedido.removeItemPedido(itemPedido.itemPedidoId());

        mapeadorEntidadePedido.mesclar(entidadePersistenciaPedido, pedido);

        Assertions.assertThat(entidadePersistenciaPedido.getItems()).isNotEmpty();
        Assertions.assertThat(entidadePersistenciaPedido.getItems().size()).isEqualTo(pedido.itensPedido().size());
    }

}
