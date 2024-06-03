package com.kakaobank.order.payment;

import com.kakaobank.order.common.entity.Payment;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

	Payment findByOrderId(String orderId);

}
