package com.kakaobank.order.order.dto;

public record CartResponse(long id, long productId, String productName, int price, int quantity, boolean available) {
	public static CartResponse of(CartItemInfo cartItemInfo, boolean available) {
		return new CartResponse(cartItemInfo.getId(), cartItemInfo.getProductId(), cartItemInfo.getProductName(),
				cartItemInfo.getPrice().intValueExact(), cartItemInfo.getQuantity(), available);
	}
}
