package com.kakaobank.order.member.dto;

import com.kakaobank.order.common.entity.Member.Gender;

import java.time.LocalDate;

public record SignupRequest(String userId, String password, String name, String phone, LocalDate birth, Gender gender) {
}
