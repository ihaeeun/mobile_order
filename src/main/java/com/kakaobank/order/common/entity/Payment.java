package com.kakaobank.order.common.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Table(indexes = @Index(name = "idx_orderId", columnList = "order_id"))
public class Payment {

    @Id
    @GeneratedValue
    private long id;

    private String orderId;

    private boolean cancellation = false;

    public Payment(String orderId) {
        this.orderId = orderId;
    }

    public long getId() {
        return id;
    }

}
