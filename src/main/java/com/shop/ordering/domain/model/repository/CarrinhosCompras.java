package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.CarrinhoCompras;
import com.shop.ordering.domain.model.valueobject.id.CarrinhoComprasId;
import com.shop.ordering.domain.model.valueobject.id.ClienteId;

import java.util.Optional;

public interface CarrinhosCompras extends Removivel<CarrinhoCompras, CarrinhoComprasId> {

    Optional<CarrinhoCompras> doCliente(ClienteId clienteId);
}
