package com.kakaobank.order.order.dto;

public record CartResponse(long id, long productId, String productName, int price, int quantity, boolean available) {
}