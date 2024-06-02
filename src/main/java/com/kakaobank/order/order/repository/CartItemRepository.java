package com.kakaobank.order.order.repository;

import com.kakaobank.order.common.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    List<CartItem> findByUserId(String userId);

    CartItem findByUserIdAndProductId(String userId, long productId);
}
