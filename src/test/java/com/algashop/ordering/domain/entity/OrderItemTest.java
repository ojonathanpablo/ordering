package com.algashop.ordering.domain.entity;


import com.algashop.ordering.domain.valueobject.Money;
import com.algashop.ordering.domain.valueobject.ProductName;
import com.algashop.ordering.domain.valueobject.Quantity;
import com.algashop.ordering.domain.valueobject.id.OrderId;
import com.algashop.ordering.domain.valueobject.id.ProductId;
import org.junit.jupiter.api.Test;

class OrderItemTest {


    @Test
    public void shoulGenerate() {
        OrderItem.brandNew()
                .productId(new ProductId())
                .id(new OrderId())
                .productName(new ProductName(" Mouse "))
                .price(new Money("100"))
                .quantity(new Quantity(1))
                .build();
    }
}