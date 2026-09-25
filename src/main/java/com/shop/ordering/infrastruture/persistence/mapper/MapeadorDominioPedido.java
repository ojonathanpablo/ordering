package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.ItemPedido;
import com.shop.ordering.domain.model.entity.MetodoPagamento;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.entity.StatusPedido;
import com.shop.ordering.domain.model.valueobject.CEP;
import com.shop.ordering.domain.model.valueobject.Cobranca;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.Documento;
import com.shop.ordering.domain.model.valueobject.Endereco;
import com.shop.ordering.domain.model.valueobject.Entrega;
import com.shop.ordering.domain.model.valueobject.NomeCompleto;
import com.shop.ordering.domain.model.valueobject.NomeProduto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.Recebedor;
import com.shop.ordering.domain.model.valueobject.Telefone;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.ItemPedidoId;
import com.shop.ordering.domain.model.valueobject.id.PedidoId;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;
import com.shop.ordering.infrastruture.persistence.embeddable.CobrancaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EntregaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.RecebedorEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaItemPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapeadorDominioPedido {

    public Pedido paraDominio(EntidadePersistenciaPedido entidadePersistenciaPedido) {
        return Pedido.builder()
                .id(new PedidoId(entidadePersistenciaPedido.getId()))
                .clienteId(new ClienteId(entidadePersistenciaPedido.getClienteId()))
                .valorTotal(new Dinheiro(entidadePersistenciaPedido.getValorTotal()))
                .totalItens(new Quantidade(entidadePersistenciaPedido.getQuantidade()))
                .status(StatusPedido.valueOf(entidadePersistenciaPedido.getStatus()))
                .metodoPagamento(MetodoPagamento.valueOf(entidadePersistenciaPedido.getMetodoPagamento()))
                .realizadoEm(entidadePersistenciaPedido.getRealizadoEm())
                .pagoEm(entidadePersistenciaPedido.getPagoEm())
                .canceladoEm(entidadePersistenciaPedido.getCanceladoEm())
                .prontoEm(entidadePersistenciaPedido.getProntoEm())
                .cobranca(mapearCobranca(entidadePersistenciaPedido.getCobranca()))
                .entrega(mapearEntrega(entidadePersistenciaPedido.getEntrega()))
                .itens(mapearItens(entidadePersistenciaPedido))
                .versao(entidadePersistenciaPedido.getVersion())
                .existente();
    }

    private Set<ItemPedido> mapearItens(EntidadePersistenciaPedido entidadePersistenciaPedido) {
        Set<EntidadePersistenciaItemPedido> itensPersistencia = entidadePersistenciaPedido.getItems();

        if (itensPersistencia == null || itensPersistencia.isEmpty()) {
            return new HashSet<>();
        }

        return itensPersistencia.stream()
                .map(itemPersistencia -> mapearItem(entidadePersistenciaPedido, itemPersistencia))
                .collect(Collectors.toSet());
    }

    private ItemPedido mapearItem(EntidadePersistenciaPedido entidadePersistenciaPedido,
                                   EntidadePersistenciaItemPedido itemPersistencia) {
        return ItemPedido.existente()
                .id(new ItemPedidoId(itemPersistencia.getId()))
                .pedidoId(new PedidoId(entidadePersistenciaPedido.getId()))
                .produtoId(new ProdutoId(itemPersistencia.getProdutoId()))
                .nomeProduto(new NomeProduto(itemPersistencia.getProdutoNome()))
                .preco(new Dinheiro(itemPersistencia.getPreco()))
                .quantidade(new Quantidade(itemPersistencia.getQuantidade()))
                .valorTotal(new Dinheiro(itemPersistencia.getValorTotal()))
                .build();
    }

    private Cobranca mapearCobranca(CobrancaEmbeddable cobrancaEmbeddable) {
        if (cobrancaEmbeddable == null) {
            return null;
        }

        NomeCompleto nomeCompleto = new NomeCompleto(cobrancaEmbeddable.getPrimeiroNome(), cobrancaEmbeddable.getUltimoNome());
        Recebedor recebedor = new Recebedor(nomeCompleto,
                new Documento(cobrancaEmbeddable.getDocumento()),
                new Telefone(cobrancaEmbeddable.getTelefone()));

        return new Cobranca(recebedor, mapearEndereco(cobrancaEmbeddable.getEndereco()));
    }

    private Entrega mapearEntrega(EntregaEmbeddable entregaEmbeddable) {
        if (entregaEmbeddable == null) {
            return null;
        }

        return new Entrega(
                new Dinheiro(entregaEmbeddable.getCusto()),
                entregaEmbeddable.getDataPrevista(),
                mapearRecebedor(entregaEmbeddable.getRecebedor()),
                mapearEndereco(entregaEmbeddable.getEndereco())
        );
    }

    private Recebedor mapearRecebedor(RecebedorEmbeddable recebedorEmbeddable) {
        if (recebedorEmbeddable == null) {
            return null;
        }

        NomeCompleto nomeCompleto = new NomeCompleto(recebedorEmbeddable.getPrimeiroNome(), recebedorEmbeddable.getUltimoNome());
        return new Recebedor(nomeCompleto,
                new Documento(recebedorEmbeddable.getDocumento()),
                new Telefone(recebedorEmbeddable.getTelefone()));
    }

    private Endereco mapearEndereco(EnderecoEmbeddable enderecoEmbeddable) {
        if (enderecoEmbeddable == null) {
            return null;
        }

        return Endereco.builder()
                .rua(enderecoEmbeddable.getRua())
                .numero(enderecoEmbeddable.getNumero())
                .complemento(enderecoEmbeddable.getComplemento())
                .bairro(enderecoEmbeddable.getBairro())
                .cidade(enderecoEmbeddable.getCidade())
                .estado(enderecoEmbeddable.getEstado())
                .cep(new CEP(enderecoEmbeddable.getCep()))
                .build();
    }

}
