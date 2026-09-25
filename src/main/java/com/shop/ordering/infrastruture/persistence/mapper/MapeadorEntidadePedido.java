package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.ItemPedido;
import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.valueobject.Cobranca;
import com.shop.ordering.domain.model.valueobject.Endereco;
import com.shop.ordering.domain.model.valueobject.Entrega;
import com.shop.ordering.domain.model.valueobject.Recebedor;
import com.shop.ordering.infrastruture.persistence.embeddable.CobrancaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EntregaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.RecebedorEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaItemPedido;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapeadorEntidadePedido {

    public EntidadePersistenciaPedido paraEntidade(Pedido pedido) {
        return mesclar(new EntidadePersistenciaPedido(), pedido);
    }

    public EntidadePersistenciaPedido mesclar(EntidadePersistenciaPedido entidadePersistenciaPedido, Pedido pedido) {
        entidadePersistenciaPedido.setId(pedido.id().valor().toLong());
        entidadePersistenciaPedido.setClienteId(pedido.clienteId().valor());
        entidadePersistenciaPedido.setValorTotal(pedido.valorTotal().valor());
        entidadePersistenciaPedido.setQuantidade(pedido.quantidade().valor());
        entidadePersistenciaPedido.setStatus(pedido.statusPedido().name());
        entidadePersistenciaPedido.setMetodoPagamento(pedido.metodoPagamento().name());
        entidadePersistenciaPedido.setRealizadoEm(pedido.realizadoEm());
        entidadePersistenciaPedido.setPagoEm(pedido.pagoEm());
        entidadePersistenciaPedido.setCanceladoEm(pedido.canceladoEm());
        entidadePersistenciaPedido.setProntoEm(pedido.prontoEm());
        entidadePersistenciaPedido.setVersion(pedido.versao());
        entidadePersistenciaPedido.setCobranca(mapearCobranca(pedido.cobranca()));
        entidadePersistenciaPedido.setEntrega(mapearEntrega(pedido.entrega()));

        Set<EntidadePersistenciaItemPedido> itensMesclados = mesclarItens(pedido, entidadePersistenciaPedido);
        entidadePersistenciaPedido.replaceItems(itensMesclados);

        return entidadePersistenciaPedido;
    }

    private Set<EntidadePersistenciaItemPedido> mesclarItens(Pedido pedido, EntidadePersistenciaPedido entidadePersistenciaPedido) {
        Set<ItemPedido> itensNovosOuAtualizados = pedido.itensPedido();

        if (itensNovosOuAtualizados == null || itensNovosOuAtualizados.isEmpty()) {
            return new HashSet<>();
        }

        Set<EntidadePersistenciaItemPedido> itensExistentes = entidadePersistenciaPedido.getItems();
        if (itensExistentes == null || itensExistentes.isEmpty()) {
            return itensNovosOuAtualizados.stream()
                    .map(this::paraEntidade)
                    .collect(Collectors.toSet());
        }

        Map<Long, EntidadePersistenciaItemPedido> mapaItensExistentes = itensExistentes.stream()
                .collect(Collectors.toMap(EntidadePersistenciaItemPedido::getId, item -> item));

        return itensNovosOuAtualizados.stream()
                .map(itemPedido -> {
                    EntidadePersistenciaItemPedido entidadeItemPedido = mapaItensExistentes.getOrDefault(
                            itemPedido.itemPedidoId().valor().toLong(), new EntidadePersistenciaItemPedido()
                    );
                    return mesclar(entidadeItemPedido, itemPedido);
                })
                .collect(Collectors.toSet());
    }

    public EntidadePersistenciaItemPedido paraEntidade(ItemPedido itemPedido) {
        return mesclar(new EntidadePersistenciaItemPedido(), itemPedido);
    }

    private EntidadePersistenciaItemPedido mesclar(EntidadePersistenciaItemPedido entidadeItemPedido, ItemPedido itemPedido) {
        entidadeItemPedido.setId(itemPedido.itemPedidoId().valor().toLong());
        entidadeItemPedido.setProdutoId(itemPedido.produtoId().valor());
        entidadeItemPedido.setProdutoNome(itemPedido.nomeProduto().valor());
        entidadeItemPedido.setPreco(itemPedido.preco().valor());
        entidadeItemPedido.setQuantidade(itemPedido.quantidade().valor());
        entidadeItemPedido.setValorTotal(itemPedido.valorTotal().valor());
        return entidadeItemPedido;
    }

    private CobrancaEmbeddable mapearCobranca(Cobranca cobranca) {
        if (cobranca == null) {
            return null;
        }

        Recebedor recebedor = cobranca.recebedor();

        return CobrancaEmbeddable.builder()
                .primeiroNome(recebedor.nomeCompleto().primeiroNome())
                .ultimoNome(recebedor.nomeCompleto().ultimoNome())
                .documento(recebedor.documento().valor())
                .telefone(recebedor.telefone().valor())
                .endereco(mapearEndereco(cobranca.endereco()))
                .build();
    }

    private EntregaEmbeddable mapearEntrega(Entrega entrega) {
        if (entrega == null) {
            return null;
        }

        return EntregaEmbeddable.builder()
                .custo(entrega.custo().valor())
                .dataPrevista(entrega.dataPrevista())
                .recebedor(mapearRecebedor(entrega.recebedor()))
                .endereco(mapearEndereco(entrega.endereco()))
                .build();
    }

    private RecebedorEmbeddable mapearRecebedor(Recebedor recebedor) {
        if (recebedor == null) {
            return null;
        }

        return RecebedorEmbeddable.builder()
                .primeiroNome(recebedor.nomeCompleto().primeiroNome())
                .ultimoNome(recebedor.nomeCompleto().ultimoNome())
                .documento(recebedor.documento().valor())
                .telefone(recebedor.telefone().valor())
                .build();
    }

    private EnderecoEmbeddable mapearEndereco(Endereco endereco) {
        if (endereco == null) {
            return null;
        }

        return EnderecoEmbeddable.builder()
                .rua(endereco.rua())
                .numero(endereco.numero())
                .complemento(endereco.complemento())
                .bairro(endereco.bairro())
                .cidade(endereco.cidade())
                .estado(endereco.estado())
                .cep(endereco.cep().valor())
                .build();
    }

}
