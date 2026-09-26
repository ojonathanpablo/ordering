package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.Cliente;
import com.shop.ordering.domain.model.valueobject.Email;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;

import java.util.Optional;

public interface Clientes extends Repository<Cliente, ClienteId> {

    Optional<Cliente> deEmail(Email email);

    boolean eEmailUnico(Email email, ClienteId exceptCustomerId);
}
