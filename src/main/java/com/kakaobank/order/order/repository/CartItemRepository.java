package com.kakaobank.order.order.repository;

import java.util.List;

import com.kakaobank.order.common.entity.CartItem;
import com.kakaobank.order.order.dto.CartItemInfo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {

	List<CartItem> findByMemberId(String memberId);

	@Query("select new com.kakaobank.order.order.dto.CartItemInfo(c.id, c.productId, p.name, p.price, c.quantity, p.stock) " +
			"from CartItem c " +
			"join Product p on c.productId = p.id " +
			"where c.memberId = :memberId")
	List<CartItemInfo> findCartItemResponseByMemberId(@Param("memberId") String memberId);

	@Query("select new com.kakaobank.order.order.dto.CartItemInfo(c.id, c.productId, p.name, p.price, c.quantity, p.stock) " +
			"from CartItem c " +
			"join Product p on c.productId = p.id " +
			" where c.id IN :cartIds")
	List<CartItemInfo> findAllCartItemInfoById(@Param("cartIds") List<Long> cartIds);

	CartItem findByMemberIdAndProductId(String memberId, long productId);

}
