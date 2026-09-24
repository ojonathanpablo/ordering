package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.RaizDeAgregado;

import java.util.Optional;

public interface Repository<T extends RaizDeAgregado<ID>, ID> {

    Optional<T> porId(ID id);

    boolean existe(ID id);

    void adicionar(T raizDeAgregado);

    int contar();
}
