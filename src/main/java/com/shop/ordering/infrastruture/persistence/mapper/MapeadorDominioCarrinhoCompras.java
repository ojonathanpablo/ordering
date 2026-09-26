package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.CarrinhoCompras;
import com.shop.ordering.domain.model.entity.ItemCarrinhoCompras;
import com.shop.ordering.domain.model.valueobject.Dinheiro;
import com.shop.ordering.domain.model.valueobject.NomeProduto;
import com.shop.ordering.domain.model.valueobject.Quantidade;
import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.domain.model.valueobject.id.ItemCarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ProdutoId;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaItemCarrinhoCompras;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class MapeadorDominioCarrinhoCompras {

    public CarrinhoCompras paraDominio(EntidadePersistenciaCarrinhoCompras entidadePersistencia) {
        return CarrinhoCompras.existente()
                .carrinhoComprasId(new CarrinhoComprasId(entidadePersistencia.getId()))
                .clienteId(new ClienteId(entidadePersistencia.getClienteId()))
                .valorTotal(new Dinheiro(entidadePersistencia.getValorTotal()))
                .totalDeItens(new Quantidade(entidadePersistencia.getTotalDeItens()))
                .criadoEm(entidadePersistencia.getCriadoEm())
                .itens(mapearItens(entidadePersistencia))
                .versao(entidadePersistencia.getVersao())
                .build();
    }

    private Set<ItemCarrinhoCompras> mapearItens(EntidadePersistenciaCarrinhoCompras entidadePersistencia) {
        Set<EntidadePersistenciaItemCarrinhoCompras> itensPersistencia = entidadePersistencia.getItens();

        if (itensPersistencia == null || itensPersistencia.isEmpty()) {
            return new HashSet<>();
        }

        return itensPersistencia.stream()
                .map(itemPersistencia -> mapearItem(entidadePersistencia, itemPersistencia))
                .collect(Collectors.toSet());
    }

    private ItemCarrinhoCompras mapearItem(EntidadePersistenciaCarrinhoCompras entidadePersistencia,
                                           EntidadePersistenciaItemCarrinhoCompras itemPersistencia) {
        return ItemCarrinhoCompras.existente()
                .itemCarrinhoComprasId(new ItemCarrinhoComprasId(itemPersistencia.getId()))
                .carrinhoComprasId(new CarrinhoComprasId(entidadePersistencia.getId()))
                .produtoId(new ProdutoId(itemPersistencia.getProdutoId()))
                .nomeProduto(new NomeProduto(itemPersistencia.getNomeProduto()))
                .preco(new Dinheiro(itemPersistencia.getPreco()))
                .quantidade(new Quantidade(itemPersistencia.getQuantidade()))
                .valorTotal(new Dinheiro(itemPersistencia.getValorTotal()))
                .disponivel(itemPersistencia.getDisponivel())
                .build();
    }
}
