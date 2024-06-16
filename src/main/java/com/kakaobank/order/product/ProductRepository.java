package com.kakaobank.order.product;

import com.kakaobank.order.common.entity.Product;
import jakarta.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

	@Modifying
	@Transactional
	@Query(value = "update Product p set p.stock = p.stock - :quantity where p.id = :productId")
	void updateStock(@Param("productId") long productId, @Param("quantity") int quantity);

}
