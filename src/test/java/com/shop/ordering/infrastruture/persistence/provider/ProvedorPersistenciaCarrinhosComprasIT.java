package com.shop.ordering.infrastruture.persistence.provider;

import com.shop.ordering.domain.model.entity.CarrinhoCompras;
import com.shop.ordering.domain.model.entity.CarrinhoComprasTestDataBuilder;
import com.shop.ordering.domain.model.entity.ItemCarrinhoCompras;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.infrastruture.persistence.config.ConfigAuditoriaDadosSpring;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaClienteTestDataBuilder;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorDominioCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.mapper.MapeadorEntidadeCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@DataJpaTest
@Import({
        ProvedorPersistenciaCarrinhosCompras.class,
        MapeadorEntidadeCarrinhoCompras.class,
        MapeadorDominioCarrinhoCompras.class,
        ConfigAuditoriaDadosSpring.class
})
class ProvedorPersistenciaCarrinhosComprasIT {

    private final ProvedorPersistenciaCarrinhosCompras provedorPersistenciaCarrinhosCompras;
    private final RepositorioPersistenciaCarrinhoCompras repositorioPersistenciaCarrinhoCompras;
    private final RepositorioPersistenciaCliente repositorioPersistenciaCliente;

    private ClienteId clienteId;

    @Autowired
    public ProvedorPersistenciaCarrinhosComprasIT(ProvedorPersistenciaCarrinhosCompras provedorPersistenciaCarrinhosCompras,
                                                  RepositorioPersistenciaCarrinhoCompras repositorioPersistenciaCarrinhoCompras,
                                                  RepositorioPersistenciaCliente repositorioPersistenciaCliente) {
        this.provedorPersistenciaCarrinhosCompras = provedorPersistenciaCarrinhosCompras;
        this.repositorioPersistenciaCarrinhoCompras = repositorioPersistenciaCarrinhoCompras;
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
    public void devePersistirCarrinhoComMultiplosItens() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();

        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        EntidadePersistenciaCarrinhoCompras entidade = repositorioPersistenciaCarrinhoCompras
                .findById(carrinho.id().valor().toLong()).orElseThrow();

        Assertions.assertThat(entidade.getClienteId()).isEqualTo(clienteId.valor());
        Assertions.assertThat(entidade.getTotalDeItens()).isEqualTo(carrinho.totalDeItens().valor());
        Assertions.assertThat(entidade.getValorTotal()).isEqualByComparingTo(carrinho.valorTotal().valor());
        Assertions.assertThat(entidade.getItens()).hasSize(carrinho.itens().size());
        Assertions.assertThat(carrinho.versao()).isZero();
    }

    @Test
    public void deveBuscarPorClienteEValidarItens() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        CarrinhoCompras encontrado = provedorPersistenciaCarrinhosCompras.doCliente(clienteId).orElseThrow();

        Assertions.assertThat(encontrado.id()).isEqualTo(carrinho.id());
        Assertions.assertThat(encontrado.clienteId()).isEqualTo(clienteId);
        Assertions.assertThat(encontrado.itens())
                .extracting(ItemCarrinhoCompras::itemCarrinhoComprasId)
                .containsExactlyInAnyOrderElementsOf(
                        carrinho.itens().stream().map(ItemCarrinhoCompras::itemCarrinhoComprasId).toList()
                );
        Assertions.assertThat(encontrado.itens())
                .extracting(ItemCarrinhoCompras::produtoId)
                .containsExactlyInAnyOrderElementsOf(
                        carrinho.itens().stream().map(ItemCarrinhoCompras::produtoId).toList()
                );
    }

    @Test
    public void naoDeveEncontrarCarrinhoDeClienteSemCarrinho() {
        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.doCliente(clienteId)).isEmpty();
    }

    @Test
    public void deveRemoverCarrinho() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.existe(carrinho.id())).isTrue();

        provedorPersistenciaCarrinhosCompras.remover(carrinho);

        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.existe(carrinho.id())).isFalse();
        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.porId(carrinho.id())).isEmpty();
    }

    @Test
    public void deveRemoverCarrinhoPorId() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        provedorPersistenciaCarrinhosCompras.remove(carrinho.id());

        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.existe(carrinho.id())).isFalse();
    }

    @Test
    public void deveAtualizarCarrinhoEIncrementarVersao() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);
        Assertions.assertThat(carrinho.versao()).isZero();

        carrinho = provedorPersistenciaCarrinhosCompras.porId(carrinho.id()).orElseThrow();
        ItemCarrinhoCompras item = carrinho.itens().iterator().next();
        carrinho.alterarQuantidadeDoItem(item.itemCarrinhoComprasId(), new Quantidade(5));
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        Assertions.assertThat(carrinho.versao()).isEqualTo(1L);

        EntidadePersistenciaCarrinhoCompras entidade = repositorioPersistenciaCarrinhoCompras
                .findById(carrinho.id().valor().toLong()).orElseThrow();
        Assertions.assertThat(entidade.getVersao()).isEqualTo(1L);
        Assertions.assertThat(entidade.getTotalDeItens()).isEqualTo(carrinho.totalDeItens().valor());
    }

    @Test
    public void deveRemoverItemDoBancoAoRemoverDoCarrinho() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        carrinho = provedorPersistenciaCarrinhosCompras.porId(carrinho.id()).orElseThrow();
        ItemCarrinhoCompras item = carrinho.itens().iterator().next();
        carrinho.removerItem(item.itemCarrinhoComprasId());
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        EntidadePersistenciaCarrinhoCompras entidade = repositorioPersistenciaCarrinhoCompras
                .findById(carrinho.id().valor().toLong()).orElseThrow();
        Assertions.assertThat(entidade.getItens()).hasSize(1);
        Assertions.assertThat(entidade.getItens())
                .noneMatch(i -> i.getId().equals(item.itemCarrinhoComprasId().valor().toLong()));
    }

    @Test
    public void deveContar() {
        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.contar()).isZero();

        provedorPersistenciaCarrinhosCompras.adicionar(
                CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build()
        );

        Assertions.assertThat(provedorPersistenciaCarrinhosCompras.contar()).isEqualTo(1);
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    @DirtiesContext(methodMode = DirtiesContext.MethodMode.AFTER_METHOD)
    public void deveAdicionarEEncontrarSemFalharSemTransacao() {
        CarrinhoCompras carrinho = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().clienteId(clienteId).build();
        provedorPersistenciaCarrinhosCompras.adicionar(carrinho);

        Assertions.assertThatNoException().isThrownBy(
                () -> provedorPersistenciaCarrinhosCompras.porId(carrinho.id()).orElseThrow()
        );
    }

}
