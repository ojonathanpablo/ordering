package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaPedido;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RepositorioPersistenciaPedido extends JpaRepository<EntidadePersistenciaPedido, Long> {
}
