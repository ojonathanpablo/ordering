package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.*;
import com.algashop.ordering.domain.valueobject.id.ClienteId;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class ClienteTestDataBuilder {
    private ClienteTestDataBuilder() {
    }

    public static Cliente.ClienteNovoBuilder clienteNovo() {
        return Cliente.novo()
                .nomeCompleto(new NomeCompleto("John","Doe"))
                .dataNascimento(new DataNascimento(LocalDate.of(1991, 7,5)))
                .email(new Email("johndoe@email.com"))
                .telefone(new Telefone("478-256-2604"))
                .documento(new Documento("255-08-0578"))
                .notificacoesPromocionaisPermitidas(true)
                .endereco(Endereco.builder()
                        .rua("Bourbon Street")
                        .numero("1134")
                        .bairro("North Ville")
                        .cidade("York")
                        .estado("South California")
                        .cep(new CEP("12345"))
                        .complemento("Apt. 114")
                        .build());
    }

    public static Cliente.ClienteExistenteBuilder clienteExistente() {
        return Cliente.existente()
                .id(new ClienteId())
                .registradoEm(OffsetDateTime.now())
                .notificacoesPromocionaisPermitidas(true)
                .arquivado(false)
                .arquivadoEm(null)
                .nomeCompleto(new NomeCompleto("John","Doe"))
                .dataNascimento(new DataNascimento(LocalDate.of(1991, 7,5)))
                .email(new Email("johndoe@email.com"))
                .telefone(new Telefone("478-256-2604"))
                .documento(new Documento("255-08-0578"))
                .notificacoesPromocionaisPermitidas(true)
                .pontosFidelidade(PontosFidelidade.ZERO)
                .endereco(Endereco.builder()
                        .rua("Bourbon Street")
                        .numero("1134")
                        .bairro("North Ville")
                        .cidade("York")
                        .estado("South California")
                        .cep(new CEP("12345"))
                        .complemento("Apt. 114")
                        .build())
                ;
    }

    public static Cliente.ClienteExistenteBuilder clienteExistenteAnonimizado() {
        return Cliente.existente()
                .id(new ClienteId())
                .nomeCompleto(new NomeCompleto("Anonymous", "Anonymous"))
                .dataNascimento(null)
                .email(new Email("anonymous@anonymous.com"))
                .telefone(new Telefone("000-000-0000"))
                .documento(new Documento("000-00-0000"))
                .notificacoesPromocionaisPermitidas(false)
                .arquivado(true)
                .registradoEm(OffsetDateTime.now())
                .arquivadoEm(OffsetDateTime.now())
                .pontosFidelidade(new PontosFidelidade(10))
                .endereco(Endereco.builder()
                        .rua("Bourbon Street")
                        .numero("1134")
                        .bairro("North Ville")
                        .cidade("York")
                        .estado("South California")
                        .cep(new CEP("12345"))
                        .complemento("Apt. 114")
                        .build());
    }
}
