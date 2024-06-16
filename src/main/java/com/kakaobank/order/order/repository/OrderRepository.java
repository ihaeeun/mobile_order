package com.kakaobank.order.order.repository;

import java.util.List;

import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.OrderStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

	List<Order> findAllByMemberId(String memberId);

	Order findByIdAndOrderStatus(String id, OrderStatus status);

}
