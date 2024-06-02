package com.kakaobank.order.common.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(indexes = @Index(name = "ix_orderId", columnList = "order_id"))
public class OrderItem {
    @Id
    @GeneratedValue
    private long id;

    private String orderId;

    private long productId;

    private int quantity;

    private int price;

    public OrderItem(String orderId, long productId, int quantity, int price) {
        this.orderId = orderId;
        this.productId = productId;
        this.quantity = quantity;
        this.price = price;
    }
}