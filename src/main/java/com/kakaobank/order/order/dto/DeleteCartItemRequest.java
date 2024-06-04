package com.kakaobank.order.order.dto;

import java.util.List;

public record DeleteCartItemRequest(List<Long> cartItemIds) {
}
