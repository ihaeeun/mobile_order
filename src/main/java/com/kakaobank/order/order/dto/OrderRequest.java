package com.kakaobank.order.order.dto;

import java.util.List;

public record OrderRequest(List<Long> cartIds) {
}
