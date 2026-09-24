package com.shop.ordering.infrastruture.persistence.embeddable;

import jakarta.persistence.Embedded;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Embeddable
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class CobrancaEmbeddable {
    private String primeiroNome;
    private String ultimoNome;
    private String documento;
    private String telefone;
    @Embedded
    private EnderecoEmbeddable endereco;
}
