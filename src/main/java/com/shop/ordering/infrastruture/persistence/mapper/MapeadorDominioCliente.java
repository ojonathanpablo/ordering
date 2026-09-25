package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.Cliente;
import com.shop.ordering.domain.model.valueobject.*;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import org.springframework.stereotype.Component;

@Component
public class MapeadorDominioCliente {

    public Cliente paraDominio(EntidadePersistenciaCliente entidadePersistenciaCliente) {
        return Cliente.existente()
                .id(new ClienteId(entidadePersistenciaCliente.getId()))
                .dataNascimento(new DataNascimento(entidadePersistenciaCliente.getDataNascimento()))
                .email(new Email(entidadePersistenciaCliente.getEmail()))
                .telefone(new Telefone(entidadePersistenciaCliente.getTelefone()))
                .documento(new Documento(entidadePersistenciaCliente.getDocumento()))
                .notificacoesPromocionaisPermitidas(entidadePersistenciaCliente.getNotificacoesPromocionaisPermitidas())
                .arquivado(entidadePersistenciaCliente.getArquivado())
                .registradoEm(entidadePersistenciaCliente.getRegistradoEm())
                .arquivadoEm(entidadePersistenciaCliente.getArquivadoEm())
                .pontosFidelidade(new PontosFidelidade(entidadePersistenciaCliente.getPontosFidelidade()))
                .nomeCompleto(new NomeCompleto(entidadePersistenciaCliente.getNomeCompleto().getPrimeiroNome(), entidadePersistenciaCliente.getNomeCompleto().getUltimoNome()))
                .endereco(mapearEndereco(entidadePersistenciaCliente.getEndereco()))
                .versao(entidadePersistenciaCliente.getVersao())
                .build();
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
