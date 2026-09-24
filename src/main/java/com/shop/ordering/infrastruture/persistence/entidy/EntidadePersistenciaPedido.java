package com.shop.ordering.infrastruture.persistence.entidy;

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


}
