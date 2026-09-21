package com.shop.ordering.infrastruture.persistence.entidy;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

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


}
