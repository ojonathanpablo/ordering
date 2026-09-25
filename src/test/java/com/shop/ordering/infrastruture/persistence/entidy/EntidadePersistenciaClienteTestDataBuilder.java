package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.domain.model.utility.GeradorId;
import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.NomeCompletoEmbeddable;
import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente.EntidadePersistenciaClienteBuilder;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class EntidadePersistenciaClienteTestDataBuilder {

    private EntidadePersistenciaClienteTestDataBuilder() {
    }

    public static EntidadePersistenciaClienteBuilder clienteExistente() {
        return EntidadePersistenciaCliente.builder()
                .id(GeradorId.gerarUUIDBaseadoTempo())
                .nomeCompleto(NomeCompletoEmbeddable.builder()
                        .primeiroNome("John")
                        .ultimoNome("Doe")
                        .build())
                .dataNascimento(LocalDate.of(1991, 7, 5))
                .email("johndoe@email.com")
                .telefone("478-256-2604")
                .documento("255-08-0578")
                .notificacoesPromocionaisPermitidas(true)
                .arquivado(false)
                .registradoEm(OffsetDateTime.now())
                .pontosFidelidade(0)
                .endereco(EnderecoEmbeddable.builder()
                        .rua("Bourbon Street")
                        .numero("1134")
                        .complemento("Apt. 114")
                        .bairro("North Ville")
                        .cidade("York")
                        .estado("South California")
                        .cep("12345")
                        .build());
    }
}
