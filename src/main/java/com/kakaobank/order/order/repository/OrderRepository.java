package com.kakaobank.order.order.repository;

import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {
    List<Order> findAllByUserId(String userId);

    Order findByIdAndOrderStatus(String id, OrderStatus status);
}
