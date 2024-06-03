package com.kakaobank.order.order.repository;

import java.util.List;

import com.kakaobank.order.common.entity.OrderItem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

	List<OrderItem> findAllByOrderId(String orderId);

}
