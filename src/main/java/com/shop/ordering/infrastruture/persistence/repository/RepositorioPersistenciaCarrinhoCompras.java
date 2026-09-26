package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCarrinhoCompras;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface RepositorioPersistenciaCarrinhoCompras extends JpaRepository<EntidadePersistenciaCarrinhoCompras, Long> {

    Optional<EntidadePersistenciaCarrinhoCompras> findByCliente_Id(UUID clienteId);
}
