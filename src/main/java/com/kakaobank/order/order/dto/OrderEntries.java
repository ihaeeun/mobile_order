package com.kakaobank.order.order.dto;

import java.util.List;

import com.kakaobank.order.common.entity.Order;

public record OrderEntries(List<Order> orderHistory) {
}
