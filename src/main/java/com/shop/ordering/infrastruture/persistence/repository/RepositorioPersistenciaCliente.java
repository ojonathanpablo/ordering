package com.shop.ordering.infrastruture.persistence.repository;

import com.shop.ordering.infrastruture.persistence.entidy.EntidadePersistenciaCliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface RepositorioPersistenciaCliente extends JpaRepository<EntidadePersistenciaCliente, UUID> {

}
