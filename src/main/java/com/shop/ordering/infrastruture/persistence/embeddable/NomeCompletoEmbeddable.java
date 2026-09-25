package com.shop.ordering.infrastruture.persistence.embeddable;

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
public class NomeCompletoEmbeddable {
    private String primeiroNome;
    private String ultimoNome;
}
