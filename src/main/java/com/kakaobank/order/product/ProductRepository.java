package com.kakaobank.order.product;

import com.kakaobank.order.common.entity.Product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Modifying
	@Query(value = "update product p set p.stock = p.stock - :quantity where p.id = :productId", nativeQuery = true)
	void updateStock(long productId, int quantity);
}
