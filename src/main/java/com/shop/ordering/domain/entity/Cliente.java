package com.shop.ordering.domain.entity;

import com.shop.ordering.domain.exception.ClienteArquivadoException;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.*;
import com.shop.ordering.domain.valueobject.id.ClienteId;
import lombok.Builder;

import java.time.OffsetDateTime;
import java.util.Objects;
import java.util.UUID;

import static com.shop.ordering.domain.exception.MensagensErro.*;

public class Cliente {
    private ClienteId id;
    private NomeCompleto nomeCompleto;
    private DataNascimento dataNascimento;
    private Email email;
    private Telefone telefone;
    private Documento documento;
    private Boolean notificacoesPromocionaisPermitidas;
    private Boolean arquivado;
    private OffsetDateTime registradoEm;
    private OffsetDateTime arquivadoEm;
    private PontosFidelidade pontosFidelidade;
    private Endereco endereco;

    @Builder(builderClassName = "ClienteNovoBuilder", builderMethodName = "novo")
    public static Cliente criarNovo(NomeCompleto nomeCompleto, DataNascimento dataNascimento, Email email, Telefone telefone,
                                    Documento documento, Boolean notificacoesPromocionaisPermitidas,
                                    Endereco endereco) {
        return new Cliente(
                new ClienteId(),
                nomeCompleto,
                dataNascimento,
                email,
                telefone,
                documento,
                notificacoesPromocionaisPermitidas,
                false,
                OffsetDateTime.now(),
                null,
                PontosFidelidade.ZERO,
                endereco
        );

    }

    @Builder(builderClassName = "ClienteExistenteBuilder", builderMethodName = "existente")
    private Cliente(ClienteId id, NomeCompleto nomeCompleto, DataNascimento dataNascimento, Email email, Telefone telefone,
                     Documento documento, Boolean notificacoesPromocionaisPermitidas, Boolean arquivado,
                     OffsetDateTime registradoEm, OffsetDateTime arquivadoEm, PontosFidelidade pontosFidelidade, Endereco endereco) {
        this.setId(id);
        this.setNomeCompleto(nomeCompleto);
        this.setDataNascimento(dataNascimento);
        this.setEmail(email);
        this.setTelefone(telefone);
        this.setDocumento(documento);
        this.setNotificacoesPromocionaisPermitidas(notificacoesPromocionaisPermitidas);
        this.setArquivado(arquivado);
        this.setRegistradoEm(registradoEm);
        this.setArquivadoEm(arquivadoEm);
        this.setPontosFidelidade(pontosFidelidade);
        this.setEndereco(endereco);
    }

    public void adicionarPontosFidelidade(PontosFidelidade pontosFidelidadeAdicionados) {
        verificarSeAlteravel();
        this.setPontosFidelidade(this.pontosFidelidade().somar(pontosFidelidadeAdicionados));
    }

    public void arquivar() {
        verificarSeAlteravel();
        this.setArquivado(true);
        this.setArquivadoEm(OffsetDateTime.now());
        this.setNomeCompleto(new NomeCompleto("Anonymous", "Anonymous"));
        this.setTelefone(new Telefone("000-000-0000"));
        this.setDocumento(new Documento("000-00-0000"));
        this.setEmail(new Email(UUID.randomUUID() + "@anonymous.com"));
        this.setDataNascimento(null);
        this.setNotificacoesPromocionaisPermitidas(false);

        Endereco.EnderecoBuilder enderecoBuilder = this.endereco.toBuilder();
        this.setEndereco(enderecoBuilder.numero("Anonymized").complemento(null).build());
    }

    public void habilitarNotificacoesPromocionais() {
        verificarSeAlteravel();
        this.setNotificacoesPromocionaisPermitidas(true);
    }

    public void desabilitarNotificacoesPromocionais() {
        verificarSeAlteravel();
        this.setNotificacoesPromocionaisPermitidas(false);
    }

    public void alterarNome(NomeCompleto nomeCompleto) {
        verificarSeAlteravel();
        this.setNomeCompleto(nomeCompleto);
    }

    public void alterarEmail(Email email) {
        verificarSeAlteravel();
        this.setEmail(email);
    }

    public void alterarTelefone(Telefone telefone) {
        verificarSeAlteravel();
        this.setTelefone(telefone);
    }

    public ClienteId id() {
        return id;
    }

    public NomeCompleto nomeCompleto() {
        return nomeCompleto;
    }

    public DataNascimento dataNascimento() {
        return dataNascimento;
    }

    public Email email() {
        return email;
    }

    public Telefone telefone() {
        return telefone;
    }

    public Documento documento() {
        return documento;
    }

    public Boolean isNotificacoesPromocionaisPermitidas() {
        return notificacoesPromocionaisPermitidas;
    }

    public Boolean isArquivado() {
        return arquivado;
    }

    public OffsetDateTime registradoEm() {
        return registradoEm;
    }

    public OffsetDateTime arquivadoEm() {
        return arquivadoEm;
    }

    public PontosFidelidade pontosFidelidade() {
        return pontosFidelidade;
    }

    public Endereco endereco() {
        return endereco;
    }

    public void alterarEndereco(Endereco endereco) {
        verificarSeAlteravel();
        this.setEndereco(endereco);
    }

    private void setId(ClienteId id) {
        Objects.requireNonNull(id);
        this.id = id;
    }

    private void setNomeCompleto(NomeCompleto nomeCompleto) {
        Objects.requireNonNull(nomeCompleto, ERRO_VALIDACAO_NOME_COMPLETO_NULO);
        this.nomeCompleto = nomeCompleto;
    }

    private void setDataNascimento(DataNascimento dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    private void setEmail(Email email) {
        Objects.requireNonNull(email);
        this.email = email;
    }

    private void setTelefone(Telefone telefone) {
        Objects.requireNonNull(telefone);
        this.telefone = telefone;
    }

    private void setDocumento(Documento documento) {
        Objects.requireNonNull(documento);
        this.documento = documento;
    }

    private void setNotificacoesPromocionaisPermitidas(Boolean notificacoesPromocionaisPermitidas) {
        Objects.requireNonNull(notificacoesPromocionaisPermitidas);
        this.notificacoesPromocionaisPermitidas = notificacoesPromocionaisPermitidas;
    }

    private void setArquivado(Boolean arquivado) {
        Objects.requireNonNull(arquivado);
        this.arquivado = arquivado;
    }

    private void setRegistradoEm(OffsetDateTime registradoEm) {
        Objects.requireNonNull(registradoEm);
        this.registradoEm = registradoEm;
    }

    private void setArquivadoEm(OffsetDateTime arquivadoEm) {
        this.arquivadoEm = arquivadoEm;
    }

    private void setPontosFidelidade(PontosFidelidade pontosFidelidade) {
        Objects.requireNonNull(pontosFidelidade);
        this.pontosFidelidade = pontosFidelidade;
    }

    private void setEndereco(Endereco endereco) {
        Objects.requireNonNull(endereco);
        this.endereco = endereco;
    }

    private void verificarSeAlteravel() {
        if (this.isArquivado()) {
            throw new ClienteArquivadoException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
