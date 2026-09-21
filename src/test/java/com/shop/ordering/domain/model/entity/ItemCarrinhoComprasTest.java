package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.ProdutoIncompativelNoCarrinhoException;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.NomeProduto;
import com.shop.ordering.domain.model.valueobject.Produto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ItemCarrinhoComprasId;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ItemCarrinhoComprasTest {

    @Test
    public void dadoDadosValidos_quandoCriarNovoItem_deveInicializarCorretamente() {
        Produto produto = ProdutoTestDataBuilder.umProduto()
                .nomeProduto(new NomeProduto("Notebook"))
                .preco(new Dinheiro("2000"))
                .build();

        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras()
                .produto(produto)
                .quantidade(new Quantidade(2))
                .build();

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.itemCarrinhoComprasId()).isNotNull(),
                i -> Assertions.assertThat(i.carrinhoComprasId()).isNotNull(),
                i -> Assertions.assertThat(i.produtoId()).isNotNull(),
                i -> Assertions.assertThat(i.nomeProduto()).isEqualTo(new NomeProduto("Notebook")),
                i -> Assertions.assertThat(i.preco()).isEqualTo(new Dinheiro("2000")),
                i -> Assertions.assertThat(i.quantidade()).isEqualTo(new Quantidade(2)),
                i -> Assertions.assertThat(i.disponivel()).isTrue(),
                i -> Assertions.assertThat(i.valorTotal()).isEqualTo(new Dinheiro("4000"))
        );
    }

    @Test
    public void dadoItem_quandoAlterarQuantidade_deveRecalcularValorTotal() {
        Produto produto = ProdutoTestDataBuilder.umProduto().preco(new Dinheiro("1000")).build();

        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras()
                .produto(produto)
                .quantidade(new Quantidade(1))
                .build();

        item.alterarQuantidade(new Quantidade(3));

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.quantidade()).isEqualTo(new Quantidade(3)),
                i -> Assertions.assertThat(i.valorTotal()).isEqualTo(new Dinheiro("3000"))
        );
    }

    @Test
    public void dadoItem_quandoAlterarQuantidadeParaZero_deveGerarIllegalArgumentException() {
        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras().build();

        Assertions.assertThatIllegalArgumentException()
                .isThrownBy(() -> item.alterarQuantidade(Quantidade.ZERO));
    }

    @Test
    public void dadoItem_quandoAtualizarComProdutoCompativel_deveRecalcularValorTotal() {
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras()
                .produto(produto)
                .quantidade(new Quantidade(2))
                .build();

        Produto produtoAtualizado = ProdutoTestDataBuilder.umProduto().preco(new Dinheiro("100")).build();
        item.atualizar(produtoAtualizado);

        Assertions.assertWith(item,
                i -> Assertions.assertThat(i.preco()).isEqualTo(produtoAtualizado.preco()),
                i -> Assertions.assertThat(i.valorTotal()).isEqualTo(produtoAtualizado.preco().multiplicar(new Quantidade(2)))
        );
    }

    @Test
    public void dadoItem_quandoAtualizarDisponibilidade_deveAtualizarStatus() {
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras()
                .produto(produto)
                .build();

        Produto produtoIndisponivel = ProdutoTestDataBuilder.umProduto().emEstoque(false).build();
        item.atualizar(produtoIndisponivel);

        Assertions.assertThat(item.disponivel()).isFalse();
    }

    @Test
    public void dadoItem_quandoAtualizarComProdutoIncompativel_deveGerarExcecao() {
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        ItemCarrinhoCompras item = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras()
                .produto(produto)
                .build();

        Produto produtoIncompativel = ProdutoTestDataBuilder.umProdutoAltMemoriaRam().build();

        Assertions.assertThatExceptionOfType(ProdutoIncompativelNoCarrinhoException.class)
                .isThrownBy(() -> item.atualizar(produtoIncompativel));
    }

    @Test
    public void dadoIdsIguais_quandoCompararItens_devemSerIguais() {
        CarrinhoComprasId carrinhoComprasId = new CarrinhoComprasId();
        ItemCarrinhoComprasId itemCarrinhoComprasId = new ItemCarrinhoComprasId();
        Produto produto = ProdutoTestDataBuilder.umProduto().build();

        ItemCarrinhoCompras item1 = ItemCarrinhoCompras.existente()
                .itemCarrinhoComprasId(itemCarrinhoComprasId)
                .carrinhoComprasId(carrinhoComprasId)
                .produtoId(produto.produtoId())
                .nomeProduto(new NomeProduto("Mouse"))
                .preco(new Dinheiro("100"))
                .quantidade(new Quantidade(1))
                .disponivel(true)
                .valorTotal(new Dinheiro("100"))
                .build();

        ItemCarrinhoCompras item2 = ItemCarrinhoCompras.existente()
                .itemCarrinhoComprasId(itemCarrinhoComprasId)
                .carrinhoComprasId(carrinhoComprasId)
                .produtoId(produto.produtoId())
                .nomeProduto(new NomeProduto("Notebook"))
                .preco(new Dinheiro("100"))
                .quantidade(new Quantidade(1))
                .disponivel(true)
                .valorTotal(new Dinheiro("100"))
                .build();

        Assertions.assertThat(item1).isEqualTo(item2);
        Assertions.assertThat(item1.hashCode()).isEqualTo(item2.hashCode());
    }

    @Test
    public void dadoIdsDiferentes_quandoCompararItens_naoDevemSerIguais() {
        ItemCarrinhoCompras item1 = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras().build();
        ItemCarrinhoCompras item2 = ItemCarrinhoComprasTestDataBuilder.umItemCarrinhoCompras().build();

        Assertions.assertThat(item1).isNotEqualTo(item2);
    }

}
