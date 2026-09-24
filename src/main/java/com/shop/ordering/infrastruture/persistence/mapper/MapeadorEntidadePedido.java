package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.Pedido;
import com.shop.ordering.domain.model.valueobject.Cobranca;
import com.shop.ordering.domain.model.valueobject.Endereco;
import com.shop.ordering.domain.model.valueobject.Entrega;
import com.shop.ordering.domain.model.valueobject.Recebedor;
import com.shop.ordering.infrastruture.persistence.embeddable.CobrancaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EntregaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.RecebedorEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.stereotype.Component;

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
        return entidadePersistenciaPedido;
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
