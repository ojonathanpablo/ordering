package com.shop.ordering.domain.model.repository;

import com.shop.ordering.domain.model.entity.RaizDeAgregado;

public interface Removivel<T extends RaizDeAgregado<ID>, ID> extends Repository<T, ID> {

    void remover(T raizDeAgregado);

    void remove(ID id);
}
