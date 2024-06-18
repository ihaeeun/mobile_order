package com.kakaobank.order.product.dto;

import java.util.List;

import com.kakaobank.order.common.entity.Product;

public record ProductEntries(List<ProductResponse> products) {
}
