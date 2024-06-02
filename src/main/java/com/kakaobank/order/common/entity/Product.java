package com.kakaobank.order.common.entity;

import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    private String name;

    private String description;

    private int price;

    private int stock = 0;

    public long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public int getStock() {
        return stock;
    }

    public boolean isAvailable(int stock) {
        return this.stock - stock >= 0;
    }

    public void updateStock(int stock) {
        this.stock -= stock;
    }
}
