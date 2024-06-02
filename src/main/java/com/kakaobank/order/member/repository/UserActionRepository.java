package com.kakaobank.order.member.repository;

import com.kakaobank.order.common.entity.UserAction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionRepository extends JpaRepository<UserAction, Long> {
}
