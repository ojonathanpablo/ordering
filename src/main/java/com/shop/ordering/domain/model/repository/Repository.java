package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.RaizDeAgregado;

import java.util.Optional;

public interface Repository<T extends RaizDeAgregado<ID>, ID> {

    Optional<T> ofId(ID id);

    boolean exists(ID id);

    void add(T raizDeAgregado);

    int count();
}
