package com.kakaobank.order.product.dto;

import com.kakaobank.order.common.entity.Product;

public record ProductResponse(long id, String name, int price, int stock) {
	public static ProductResponse of(Product product) {
		return new ProductResponse(product.getId(), product.getName(), product.getPrice().intValueExact(),
				product.getStock());
	}
}
