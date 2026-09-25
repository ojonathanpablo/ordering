package com.shop.ordering.infrastruture.persistence.entidy;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "pedido_item")
@Data
@ToString()
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EntidadePersistenciaItemPedido {

    @Id
    @EqualsAndHashCode.Include
    private Long id;
    private UUID produtoId;
    private String produtoNome;
    private BigDecimal preco;
    private Integer quantidade;
    private BigDecimal valorTotal;

    @JoinColumn
    @ManyToOne(optional = false)
    private EntidadePersistenciaPedido pedido;

    public Long getPedidoId() {
        if (getPedido() == null) {
            return null;
        }
        return getPedido().getId();
    }
}
