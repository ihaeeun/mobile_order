package com.kakaobank.order.payment.dto;

import com.kakaobank.order.common.entity.Payment;

public record PaymentResponse(boolean result, Payment payment) {
}
