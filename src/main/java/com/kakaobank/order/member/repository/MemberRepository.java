package com.kakaobank.order.member.repository;

import com.kakaobank.order.common.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {
    Member findByUserId(String userId);

    Member findByUserNameAndPhone(String userName, String phone);
}
