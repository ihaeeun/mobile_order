package com.kakaobank.order.common.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
@Table(indexes = {
        @Index(name = "ix_userId", columnList = "user_id"),
        @Index(name = "ix_userId_productId", columnList = "user_id, product_id")
})
public class CartItem {
    @Id
    @GeneratedValue
    private long id;

    private String userId;

    private long productId;

    private int quantity;

    public CartItem(String userId, long productId, int quantity) {
        this.userId = userId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public long getProductId() {
        return productId;
    }


    public int getQuantity() {
        return quantity;
    }

}
