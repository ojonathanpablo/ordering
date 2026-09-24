package com.shop.ordering.infrastruture.persistence.entidy;

import com.shop.ordering.infrastruture.persistence.embeddable.CobrancaEmbeddable;
import com.shop.ordering.infrastruture.persistence.embeddable.EntregaEmbeddable;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString()
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "Pedido")
@EntityListeners(AuditingEntityListener.class)
public class EntidadePersistenciaPedido {

    @Id
    private Long id;
    private UUID clienteId;

    private BigDecimal valorTotal;
    private int quantidade;
    private String status;
    private String metodoPagamento;

    private OffsetDateTime realizadoEm;
    private OffsetDateTime pagoEm;
    private OffsetDateTime canceladoEm;
    private OffsetDateTime prontoEm;

    @Version
    private Long version;

    @CreatedBy
    private UUID idDoUsuarioQueCriou;

    @LastModifiedDate
    private OffsetDateTime UltimaModificacao;

    @LastModifiedBy
    private UUID idDoUsuarioDaUltimaModificacao;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "primeiroNome", column = @Column(name = "cobranca_primeiro_nome")),
            @AttributeOverride(name = "ultimoNome", column = @Column(name = "cobranca_ultimo_nome")),
            @AttributeOverride(name = "documento", column = @Column(name = "cobranca_documento")),
            @AttributeOverride(name = "telefone", column = @Column(name = "cobranca_telefone")),
            @AttributeOverride(name = "endereco.rua", column = @Column(name = "cobranca_endereco_rua")),
            @AttributeOverride(name = "endereco.numero", column = @Column(name = "cobranca_endereco_numero")),
            @AttributeOverride(name = "endereco.complemento", column = @Column(name = "cobranca_endereco_complemento")),
            @AttributeOverride(name = "endereco.bairro", column = @Column(name = "cobranca_endereco_bairro")),
            @AttributeOverride(name = "endereco.cidade", column = @Column(name = "cobranca_endereco_cidade")),
            @AttributeOverride(name = "endereco.estado", column = @Column(name = "cobranca_endereco_estado")),
            @AttributeOverride(name = "endereco.cep", column = @Column(name = "cobranca_endereco_cep"))
    })
    private CobrancaEmbeddable cobranca;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "custo", column = @Column(name = "entrega_custo")),
            @AttributeOverride(name = "dataPrevista", column = @Column(name = "entrega_data_prevista")),
            @AttributeOverride(name = "recebedor.primeiroNome", column = @Column(name = "entrega_recebedor_primeiro_nome")),
            @AttributeOverride(name = "recebedor.ultimoNome", column = @Column(name = "entrega_recebedor_ultimo_nome")),
            @AttributeOverride(name = "recebedor.documento", column = @Column(name = "entrega_recebedor_documento")),
            @AttributeOverride(name = "recebedor.telefone", column = @Column(name = "entrega_recebedor_telefone")),
            @AttributeOverride(name = "endereco.rua", column = @Column(name = "entrega_endereco_rua")),
            @AttributeOverride(name = "endereco.numero", column = @Column(name = "entrega_endereco_numero")),
            @AttributeOverride(name = "endereco.complemento", column = @Column(name = "entrega_endereco_complemento")),
            @AttributeOverride(name = "endereco.bairro", column = @Column(name = "entrega_endereco_bairro")),
            @AttributeOverride(name = "endereco.cidade", column = @Column(name = "entrega_endereco_cidade")),
            @AttributeOverride(name = "endereco.estado", column = @Column(name = "entrega_endereco_estado")),
            @AttributeOverride(name = "endereco.cep", column = @Column(name = "entrega_endereco_cep"))
    })
    private EntregaEmbeddable entrega;

}
