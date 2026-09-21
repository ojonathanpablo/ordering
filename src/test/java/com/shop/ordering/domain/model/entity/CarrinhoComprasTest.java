package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.CarrinhoComprasNaoContemItemException;
import com.shop.ordering.domain.model.exception.ProdutoEsgotadoException;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.ItemCarrinhoComprasId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

class CarrinhoComprasTest {

    @Test
    void dadoCliente_quandoIniciarCompras_deveCriarCarrinhoVazio() {
        ClienteId clienteId = new ClienteId();

        CarrinhoCompras carrinhoCompras = CarrinhoCompras.criarNovo(clienteId);

        Assertions.assertWith(carrinhoCompras,
                c -> Assertions.assertThat(c.id()).isNotNull(),
                c -> Assertions.assertThat(c.clienteId()).isEqualTo(clienteId),
                c -> Assertions.assertThat(c.valorTotal()).isEqualTo(Dinheiro.ZERO),
                c -> Assertions.assertThat(c.totalDeItens()).isEqualTo(Quantidade.ZERO),
                c -> Assertions.assertThat(c.estaVazio()).isTrue(),
                c -> Assertions.assertThat(c.itens()).isEmpty()
        );
    }

    @Test
    void dadoCarrinhoVazio_quandoAdicionarProdutoForaDeEstoque_deveGerarExcecao() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();
        Produto produtoIndisponivel = ProdutoTestDataBuilder.umProdutoIndisponivel().build();

        Assertions.assertThatExceptionOfType(ProdutoEsgotadoException.class)
                .isThrownBy(() -> carrinhoCompras.adicionarItem(produtoIndisponivel, new Quantidade(1)));
    }

    @Test
    void dadoCarrinhoVazio_quandoAdicionarNovoItem_deveConterItemERecalcularTotais() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        carrinhoCompras.adicionarItem(produto, new Quantidade(2));

        Assertions.assertThat(carrinhoCompras.itens()).hasSize(1);

        ItemCarrinhoCompras item = carrinhoCompras.itens().iterator().next();
        Assertions.assertThat(item.produtoId()).isEqualTo(produto.produtoId());
        Assertions.assertThat(item.quantidade()).isEqualTo(new Quantidade(2));
        Assertions.assertThat(carrinhoCompras.totalDeItens()).isEqualTo(new Quantidade(2));
        Assertions.assertThat(carrinhoCompras.valorTotal())
                .isEqualTo(new Dinheiro(produto.preco().valor().multiply(new BigDecimal(2))));
    }

    @Test
    void dadoCarrinhoVazio_quandoAdicionarDoisProdutosDiferentes_deveConterDoisItens() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();

        carrinhoCompras.adicionarItem(ProdutoTestDataBuilder.umProduto().build(), new Quantidade(1));
        carrinhoCompras.adicionarItem(ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build(), new Quantidade(1));

        Assertions.assertThat(carrinhoCompras.itens()).hasSize(2);
    }

    @Test
    void dadoCarrinhoComProdutoExistente_quandoAdicionarMesmoProduto_deveIncrementarQuantidade() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        carrinhoCompras.adicionarItem(produto, new Quantidade(3));
        carrinhoCompras.adicionarItem(produto, new Quantidade(3));

        ItemCarrinhoCompras existente = carrinhoCompras.itens().iterator().next();

        Assertions.assertThat(carrinhoCompras.itens()).hasSize(1);
        Assertions.assertThat(existente.quantidade()).isEqualTo(new Quantidade(6));
    }

    @Test
    void dadoCarrinhoComItens_quandoRemoverItemExistente_deveRemoverERecalcularTotais() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        ItemCarrinhoCompras item = carrinhoCompras.itens().iterator().next();

        carrinhoCompras.removerItem(item.itemCarrinhoComprasId());

        Assertions.assertThat(carrinhoCompras.itens()).doesNotContain(item);
        Assertions.assertThat(carrinhoCompras.totalDeItens()).isEqualTo(
                new Quantidade(carrinhoCompras.itens().stream().mapToInt(i -> i.quantidade().valor()).sum())
        );
    }

    @Test
    void dadoCarrinhoComItens_quandoRemoverItemInexistente_deveGerarExcecao() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        ItemCarrinhoComprasId idAleatorio = new ItemCarrinhoComprasId();

        Assertions.assertThatExceptionOfType(CarrinhoComprasNaoContemItemException.class)
                .isThrownBy(() -> carrinhoCompras.removerItem(idAleatorio));
    }

    @Test
    void dadoCarrinhoComItens_quandoLimpar_deveRemoverTodosItensEZerarTotais() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();

        carrinhoCompras.limpar();

        Assertions.assertWith(carrinhoCompras,
                c -> Assertions.assertThat(c.estaVazio()).isTrue(),
                c -> Assertions.assertThat(c.totalDeItens()).isEqualTo(Quantidade.ZERO),
                c -> Assertions.assertThat(c.valorTotal()).isEqualTo(Dinheiro.ZERO)
        );
    }

    @Test
    void dadoCarrinhoComItens_quandoAtualizarPrecoDoItem_deveRecalcularValorTotal() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();

        Produto produto = ProdutoTestDataBuilder.umProduto().build();
        carrinhoCompras.adicionarItem(produto, new Quantidade(2));

        Produto produtoAtualizado = ProdutoTestDataBuilder.umProduto().preco(new Dinheiro("100")).build();
        carrinhoCompras.atualizarItem(produtoAtualizado);

        ItemCarrinhoCompras item = carrinhoCompras.encontrarItem(produtoAtualizado.produtoId());

        Assertions.assertThat(item.preco()).isEqualTo(new Dinheiro("100"));
        Assertions.assertThat(carrinhoCompras.valorTotal()).isEqualTo(new Dinheiro("200"));
    }

    @Test
    void dadoCarrinhoComItens_quandoDetectarItensIndisponiveis_deveRetornarTrue() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().comItens(false).build();
        Produto produto = ProdutoTestDataBuilder.umProduto().build();
        carrinhoCompras.adicionarItem(produto, new Quantidade(1));

        Produto produtoIndisponivel = ProdutoTestDataBuilder.umProduto().emEstoque(false).build();
        carrinhoCompras.atualizarItem(produtoIndisponivel);

        Assertions.assertThat(carrinhoCompras.contemItensIndisponiveis()).isTrue();
    }

    @Test
    void dadoCarrinhoComItens_quandoAlterarQuantidadeParaZero_deveGerarIllegalArgumentException() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        ItemCarrinhoCompras item = carrinhoCompras.itens().iterator().next();

        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> carrinhoCompras.alterarQuantidadeDoItem(item.itemCarrinhoComprasId(), Quantidade.ZERO));
    }

    @Test
    void dadoCarrinhoComItens_quandoAlterarQuantidadeDoItem_deveRecalcularTotalDeItens() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        ItemCarrinhoCompras item = carrinhoCompras.itens().iterator().next();

        carrinhoCompras.alterarQuantidadeDoItem(item.itemCarrinhoComprasId(), new Quantidade(5));

        Assertions.assertThat(carrinhoCompras.totalDeItens()).isEqualTo(
                new Quantidade(carrinhoCompras.itens().stream().mapToInt(i -> i.quantidade().valor()).sum())
        );
    }

    @Test
    void dadoCarrinhoComItens_quandoEncontrarItemPorId_deveRetornarItem() {
        CarrinhoCompras carrinhoCompras = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        ItemCarrinhoCompras item = carrinhoCompras.itens().iterator().next();

        ItemCarrinhoCompras encontrado = carrinhoCompras.encontrarItem(item.itemCarrinhoComprasId());

        Assertions.assertThat(encontrado).isEqualTo(item);
    }

    @Test
    void dadoIdsDiferentes_quandoCompararCarrinhos_naoDeveSerIgual() {
        CarrinhoCompras carrinhoCompras1 = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();
        CarrinhoCompras carrinhoCompras2 = CarrinhoComprasTestDataBuilder.umCarrinhoCompras().build();

        Assertions.assertThat(carrinhoCompras1).isNotEqualTo(carrinhoCompras2);
    }

}
