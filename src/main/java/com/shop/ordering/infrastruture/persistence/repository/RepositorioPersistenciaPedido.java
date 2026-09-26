package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public interface RepositorioPersistenciaPedido extends JpaRepository<EntidadePersistenciaPedido, Long> {

    @Query("""
        SELECT p
        FROM EntidadePersistenciaPedido p
        WHERE p.cliente.id = :clienteId
        AND YEAR(p.realizadoEm) = :ano
    """)
    List<EntidadePersistenciaPedido> realizadosPorClienteNoAno(
            @Param("clienteId") UUID clienteId,
            @Param("ano") Integer ano
    );

    @Query("""
        SELECT COUNT(p)
        FROM EntidadePersistenciaPedido p
        WHERE p.cliente.id = :clienteId
        AND YEAR(p.realizadoEm) = :ano
        AND p.pagoEm IS NOT NULL
        AND p.canceladoEm IS NULL
    """)
    long quantidadeVendasPorClienteNoAno(
            @Param("clienteId") UUID clienteId,
            @Param("ano") int ano
    );

    @Query("""
        SELECT COALESCE(SUM(p.valorTotal), 0)
        FROM EntidadePersistenciaPedido p
        WHERE p.cliente.id = :clienteId
        AND p.canceladoEm IS NULL
        AND p.pagoEm IS NOT NULL
    """)
    BigDecimal totalVendidoParaCliente(@Param("clienteId") UUID clienteId);
}
