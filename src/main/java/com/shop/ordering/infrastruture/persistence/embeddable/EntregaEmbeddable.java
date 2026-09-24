package com.shop.ordering.infrastruture.persistence.embeddable;

import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class EntregaEmbeddable {
    private BigDecimal custo;
    private LocalDate dataPrevista;
    @Embedded
    private EnderecoEmbeddable endereco;
    @Embedded
    private RecebedorEmbeddable recebedor;
}
