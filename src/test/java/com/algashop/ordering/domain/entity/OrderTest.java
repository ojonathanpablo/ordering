package com.algashop.ordering.domain.entity;

import com.algashop.ordering.domain.valueobject.id.CustomerId;
import org.junit.jupiter.api.Test;

class OrderTest {

    @Test
    public void shoulGenerate(){
        Order order = Order.draft(new CustomerId());
    }

}