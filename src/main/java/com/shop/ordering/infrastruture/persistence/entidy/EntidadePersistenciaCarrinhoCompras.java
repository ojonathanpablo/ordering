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
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Getter
@Setter
@ToString(exclude = "itens")
@NoArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Table(name = "carrinho_compras")
@EntityListeners(AuditingEntityListener.class)
public class EntidadePersistenciaCarrinhoCompras {

    @Id
    @EqualsAndHashCode.Include
    private Long id;

    @JoinColumn
    @ManyToOne(optional = false)
    private EntidadePersistenciaCliente cliente;

    private BigDecimal valorTotal;
    private Integer totalDeItens;

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

    @OneToMany(mappedBy = "carrinhoCompras", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<EntidadePersistenciaItemCarrinhoCompras> itens = new HashSet<>();

    @Builder
    public EntidadePersistenciaCarrinhoCompras(Long id, EntidadePersistenciaCliente cliente, BigDecimal valorTotal,
                                               Integer totalDeItens, UUID criadoPorIdDoUsuario, OffsetDateTime criadoEm,
                                               OffsetDateTime ultimaModificacaoEm, UUID ultimaModificacaoPorIdDoUsuario,
                                               Long versao, Set<EntidadePersistenciaItemCarrinhoCompras> itens) {
        this.id = id;
        this.cliente = cliente;
        this.valorTotal = valorTotal;
        this.totalDeItens = totalDeItens;
        this.criadoPorIdDoUsuario = criadoPorIdDoUsuario;
        this.criadoEm = criadoEm;
        this.ultimaModificacaoEm = ultimaModificacaoEm;
        this.ultimaModificacaoPorIdDoUsuario = ultimaModificacaoPorIdDoUsuario;
        this.versao = versao;
        replaceItens(itens);
    }

    public void replaceItens(Set<EntidadePersistenciaItemCarrinhoCompras> itensAtualizados) {
        if (itensAtualizados == null || itensAtualizados.isEmpty()) {
            this.setItens(new HashSet<>());
            return;
        }

        itensAtualizados.forEach(item -> item.setCarrinhoCompras(this));
        this.setItens(itensAtualizados);
    }

    public void addItem(EntidadePersistenciaItemCarrinhoCompras item) {
        if (item == null) {
            return;
        }

        if (this.getItens() == null) {
            this.setItens(new HashSet<>());
        }

        item.setCarrinhoCompras(this);
        this.getItens().add(item);
    }

    public UUID getClienteId() {
        if (this.cliente == null) {
            return null;
        }
        return this.cliente.getId();
    }
}
