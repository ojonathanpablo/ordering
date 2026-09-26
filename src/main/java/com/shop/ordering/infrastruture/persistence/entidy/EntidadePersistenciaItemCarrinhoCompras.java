package com.shop.ordering.infrastruture.persistence.entidy;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "carrinho_compras_item")
@Data
@ToString(exclude = "carrinhoCompras")
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class EntidadePersistenciaItemCarrinhoCompras {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private UUID produtoId;
    private String nomeProduto;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal valorTotal;
    private Boolean disponivel;

    @JoinColumn
    @ManyToOne(optional = false)
    private EntidadePersistenciaCarrinhoCompras carrinhoCompras;

    @CreatedBy
    private UUID criadoPorIdDoUsuario;

    @CreatedDate
    private OffsetDateTime criadoEm;

    @LastModifiedDate
    private OffsetDateTime ultimaModificacaoEm;

    @LastModifiedBy
    private UUID ultimaModificacaoPorIdDoUsuario;

    @Version
    private Long versao;

    public Long getCarrinhoComprasId() {
        if (getCarrinhoCompras() == null) {
            return null;
        }
        return getCarrinhoCompras().getId();
    }
}
