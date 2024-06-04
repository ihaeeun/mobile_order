package com.kakaobank.order.member.dto;

import java.time.LocalDate;

import com.kakaobank.order.common.entity.Gender;

public record SignupRequest(String userId, String password, String name, String phone, LocalDate birth, Gender gender) {
}
