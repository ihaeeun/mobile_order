package com.kakaobank.order.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

@Entity
@Setter
@Getter
@NoArgsConstructor
@Table(name="order_form", indexes = {
        @Index(name = "idx_userId", columnList = "user_id"),
        @Index(name = "ix_id_orderStatus", columnList = "id, order_status")
})
public class Order {
    @Id
    private String id;

    private String userId;

    private OrderStatus orderStatus = OrderStatus.ORDERED;

    private long totalAmount;

    private ZonedDateTime orderDateTime = ZonedDateTime.now();

    public Order(String id, String userId, int totalAmount) {
        this.id = id;
        this.userId = userId;
        this.totalAmount = totalAmount;
    }

    public String getId() {
        return this.id;
    }


}

