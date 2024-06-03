package com.kakaobank.order.order.repository;

import java.util.List;

import com.kakaobank.order.common.entity.CartItem;
import com.kakaobank.order.order.dto.CartItemInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByUserId(String userId);

	@Query(value = "select c.id, c.product_id, p.name, p.price, c.quantity, p.stock " +
			"from cart_item as c " +
			"inner join product as p on c.product_id = p.id where c.user_id = :userId",
			nativeQuery = true)
	List<CartItemInfo> findCartItemInfoByUserId(String userId);

	@Query(value = "select c.id, c.product_id, p.name, p.price, c.quantity, p.stock " +
			"from cart_item as c " +
			"inner join product as p on c.product_id = p.id where c.id IN :cartIds",
			nativeQuery = true)
	List<CartItemInfo> findAllCartItemInfoById(List<Long> cartIds);

	CartItem findByUserIdAndProductId(String userId, long productId);

}
