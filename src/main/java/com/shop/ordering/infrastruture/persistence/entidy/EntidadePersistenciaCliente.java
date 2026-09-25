package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.infrastruture.persistence.embeddable.EnderecoEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.NomeCompletoEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "cliente")
@EntityListeners(AuditingEntityListener.class)
public class EntidadePersistenciaCliente {

    @Id
    @EqualsAndHashCode.Include
    private UUID id;

    private LocalDate dataNascimento;
    private String email;
    private String telefone;
    private String documento;
    private Boolean notificacoesPromocionaisPermitidas;
    private Boolean arquivado;
    private OffsetDateTime registradoEm;
    private OffsetDateTime arquivadoEm;
    private Integer pontosFidelidade;

    @Version
    private Long versao;

    @CreatedBy
    private UUID criadoPorIDdoUsuario;

    @LastModifiedBy
    private UUID ultimaModificacaoPorIDdoUsuario;

    @LastModifiedDate
    private OffsetDateTime ultimaModificacaoEm;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "rua", column = @Column(name = "endereco_rua")),
            @AttributeOverride(name = "numero", column = @Column(name = "endereco_numero")),
            @AttributeOverride(name = "complemento", column = @Column(name = "endereco_complemento")),
            @AttributeOverride(name = "bairro", column = @Column(name = "endereco_bairro")),
            @AttributeOverride(name = "cidade", column = @Column(name = "endereco_cidade")),
            @AttributeOverride(name = "estado", column = @Column(name = "endereco_estado")),
            @AttributeOverride(name = "cep", column = @Column(name = "endereco_cep"))
    })
    private EnderecoEmbeddable endereco;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "primeiroNome", column = @Column(name = "primeiroNome")),
            @AttributeOverride(name = "ultimoNome", column = @Column(name = "ultimoNome"))

    })
    private NomeCompletoEmbeddable nomeCompleto;

}
