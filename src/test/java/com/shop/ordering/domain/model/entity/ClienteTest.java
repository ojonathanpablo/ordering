package com.shop.ordering.domain.model.entity;

import com.shop.ordering.domain.model.exception.ClienteArquivadoException;
import com.shop.ordering.domain.model.valueobject.*;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

class ClienteTest {

    @Test
    void dado_emailInvalido_quandoTentarCriarCliente_deveGerarExcecao() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> ClienteTestDataBuilder.clienteNovo()
                        .email(new Email("invalid")).build()
                );
    }

    @Test
    void dado_emailInvalido_quandoTentarAtualizarEmailCliente_deveGerarExcecao() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> cliente.alterarEmail(new Email("invalid")));
    }

    @Test
    void dado_clienteNaoArquivado_quandoArquivar_deveAnonimizar() {
        Cliente cliente = ClienteTestDataBuilder.clienteExistente().build();

        cliente.arquivar();

        Assertions.assertWith(cliente,
                c -> assertThat(c.nomeCompleto()).isEqualTo(new NomeCompleto("Anonymous","Anonymous")),
                c -> assertThat(c.email()).isNotEqualTo(new Email("john.doe@gmail.com")),
                c -> assertThat(c.telefone()).isEqualTo(new Telefone("000-000-0000")),
                c -> assertThat(c.documento()).isEqualTo(new Documento("000-00-0000")),
                c -> assertThat(c.dataNascimento()).isNull(),
                c -> assertThat(c.isNotificacoesPromocionaisPermitidas()).isFalse(),
                c -> assertThat(c.endereco()).isEqualTo(
                        Endereco.builder()
                                .rua("Bourbon Street")
                                .numero("Anonymized")
                                .bairro("North Ville")
                                .cidade("York")
                                .estado("South California")
                                .cep(new CEP("12345"))
                                .complemento(null)
                                .build()
                )
        );

    }

    @Test
    void dado_clienteArquivado_quandoTentarAtualizar_deveGerarExcecao() {
        Cliente cliente = ClienteTestDataBuilder.clienteExistenteAnonimizado().build();

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
                .isThrownBy(cliente::arquivar);

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
                .isThrownBy(()-> cliente.alterarEmail(new Email("email@gmail.com")));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
                .isThrownBy(()-> cliente.alterarTelefone(new Telefone("123-123-1111")));

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
                .isThrownBy(cliente::habilitarNotificacoesPromocionais);

        Assertions.assertThatExceptionOfType(ClienteArquivadoException.class)
                .isThrownBy(cliente::desabilitarNotificacoesPromocionais);
    }

    @Test
    void dado_clienteNovo_quandoAdicionarPontosFidelidade_deveSomarPontos() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();

        cliente.adicionarPontosFidelidade(new PontosFidelidade(10));
        cliente.adicionarPontosFidelidade(new PontosFidelidade(20));

        Assertions.assertThat(cliente.pontosFidelidade()).isEqualTo(new PontosFidelidade(30));
    }

    @Test
    void dado_clienteNovo_quandoAdicionarPontosFidelidadeInvalidos_deveGerarExcecao() {
        Cliente cliente = ClienteTestDataBuilder.clienteNovo().build();

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> cliente.adicionarPontosFidelidade(new PontosFidelidade(0)));

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(()-> cliente.adicionarPontosFidelidade(new PontosFidelidade(-10)));
    }
}
