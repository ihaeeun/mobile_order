package com.kakaobank.order.order.dto;

public interface CartItemInfo {

	long getId();

	long getProductId();

	String getProductName();

	int getPrice();

	int getQuantity();

	int getStock();
}
