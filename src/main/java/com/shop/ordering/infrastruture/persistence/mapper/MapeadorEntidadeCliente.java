package com.shop.ordering.infrastruture.persistence.mapper;

import com.shop.ordering.domain.model.entity.Cliente;
import com.shop.ordering.domain.model.valueobject.CEP;
import com.shop.ordering.domain.model.valueobject.Endereco;
import com.shop.ordering.domain.model.valueobject.NomeCompleto;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.NomeCompletoEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import org.springframework.stereotype.Component;

@Component
public class MapeadorEntidadeCliente {

    public EntidadePersistenciaCliente paraEntidade(Cliente cliente) {
        return mesclar(new EntidadePersistenciaCliente(), cliente);
    }

    public EntidadePersistenciaCliente mesclar(EntidadePersistenciaCliente persistenciaCliente, Cliente cliente) {
        persistenciaCliente.setId(cliente.id().valor());
        persistenciaCliente.setDataNascimento(cliente.dataNascimento().valor());
        persistenciaCliente.setEmail(cliente.email().valor());
        persistenciaCliente.setTelefone(cliente.telefone().valor());
        persistenciaCliente.setDocumento(cliente.documento().valor());
        persistenciaCliente.setNotificacoesPromocionaisPermitidas(cliente.isNotificacoesPromocionaisPermitidas());
        persistenciaCliente.setArquivado(cliente.isArquivado());
        persistenciaCliente.setRegistradoEm(cliente.registradoEm());
        persistenciaCliente.setArquivadoEm(cliente.arquivadoEm());
        persistenciaCliente.setPontosFidelidade(cliente.pontosFidelidade().valor());
        persistenciaCliente.setEndereco(mapearEndereco(cliente.endereco()));
        persistenciaCliente.setNomeCompleto(mapearNomeCompleto(cliente.nomeCompleto()));
        persistenciaCliente.setVersao(cliente.versao());
        return persistenciaCliente;
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

    private NomeCompletoEmbeddable mapearNomeCompleto(NomeCompleto nomeCompleto) {
        if (nomeCompleto == null) {
            return null;
        }
        return NomeCompletoEmbeddable.builder()
                .primeiroNome(nomeCompleto.primeiroNome())
                .ultimoNome(nomeCompleto.ultimoNome())
                .build();

    }

}
