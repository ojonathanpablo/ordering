package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.CarrinhoCompras;
import com.shop.ordering.domain.model.entity.ItemCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaItemCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.repository.RepositorioPersistenciaCliente;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class MapeadorEntidadeCarrinhoCompras {

    private final RepositorioPersistenciaCliente persistenciaCliente;

    public EntidadePersistenciaCarrinhoCompras paraEntidade(CarrinhoCompras carrinhoCompras) {
        return mesclar(new EntidadePersistenciaCarrinhoCompras(), carrinhoCompras);
    }

    public EntidadePersistenciaCarrinhoCompras mesclar(EntidadePersistenciaCarrinhoCompras entidadePersistencia,
                                                       CarrinhoCompras carrinhoCompras) {
        entidadePersistencia.setId(carrinhoCompras.id().valor().toLong());
        entidadePersistencia.setValorTotal(carrinhoCompras.valorTotal().valor());
        entidadePersistencia.setTotalDeItens(carrinhoCompras.totalDeItens().valor());
        entidadePersistencia.setCriadoEm(carrinhoCompras.criadoEm());
        entidadePersistencia.setVersao(carrinhoCompras.versao());

        Set<EntidadePersistenciaItemCarrinhoCompras> itensMesclados = mesclarItens(carrinhoCompras, entidadePersistencia);
        entidadePersistencia.replaceItens(itensMesclados);

        var entidadePersistenciaCliente = persistenciaCliente.getReferenceById(carrinhoCompras.clienteId().valor());
        entidadePersistencia.setCliente(entidadePersistenciaCliente);

        return entidadePersistencia;
    }

    private Set<EntidadePersistenciaItemCarrinhoCompras> mesclarItens(CarrinhoCompras carrinhoCompras,
                                                                      EntidadePersistenciaCarrinhoCompras entidadePersistencia) {
        Set<ItemCarrinhoCompras> itensNovosOuAtualizados = carrinhoCompras.itens();

        if (itensNovosOuAtualizados == null || itensNovosOuAtualizados.isEmpty()) {
            return new HashSet<>();
        }

        Set<EntidadePersistenciaItemCarrinhoCompras> itensExistentes = entidadePersistencia.getItens();
        if (itensExistentes == null || itensExistentes.isEmpty()) {
            return itensNovosOuAtualizados.stream()
                    .map(this::paraEntidade)
                    .collect(Collectors.toSet());
        }

        Map<Long, EntidadePersistenciaItemCarrinhoCompras> mapaItensExistentes = itensExistentes.stream()
                .collect(Collectors.toMap(EntidadePersistenciaItemCarrinhoCompras::getId, item -> item));

        return itensNovosOuAtualizados.stream()
                .map(item -> {
                    EntidadePersistenciaItemCarrinhoCompras entidadeItem = mapaItensExistentes.getOrDefault(
                            item.itemCarrinhoComprasId().valor().toLong(), new EntidadePersistenciaItemCarrinhoCompras()
                    );
                    return mesclar(entidadeItem, item);
                })
                .collect(Collectors.toSet());
    }

    public EntidadePersistenciaItemCarrinhoCompras paraEntidade(ItemCarrinhoCompras item) {
        return mesclar(new EntidadePersistenciaItemCarrinhoCompras(), item);
    }

    private EntidadePersistenciaItemCarrinhoCompras mesclar(EntidadePersistenciaItemCarrinhoCompras entidadeItem,
                                                            ItemCarrinhoCompras item) {
        entidadeItem.setId(item.itemCarrinhoComprasId().valor().toLong());
        entidadeItem.setProdutoId(item.produtoId().valor());
        entidadeItem.setNomeProduto(item.nomeProduto().valor());
        entidadeItem.setPreco(item.preco().valor());
        entidadeItem.setQuantidade(item.quantidade().valor());
        entidadeItem.setValorTotal(item.valorTotal().valor());
        entidadeItem.setDisponivel(item.disponivel());
        return entidadeItem;
    }
}
