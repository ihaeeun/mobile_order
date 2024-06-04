package com.kakaobank.order.order.repository;

import java.util.List;
import java.util.UUID;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.OrderItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderItemRepositoryTests {

	@Autowired
	OrderItemRepository orderItemRepository;

	private OrderItem orderItem;

	private String orderId;

	@BeforeEach
	void setUp() {
		this.orderId = UUID.randomUUID().toString();
		this.orderItem = TestUtils.buildOrderItem(this.orderId);
	}

	@Test
	void saveAll() {
		// when
		var result = this.orderItemRepository.saveAll(List.of(this.orderItem));

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0)).isNotNull();
		assertThat(result.get(0).getOrderId()).isEqualTo(this.orderId);
		assertThat(result.get(0).getProductId()).isEqualTo(this.orderItem.getProductId());
		assertThat(result.get(0).getPrice()).isEqualTo(this.orderItem.getPrice());
		assertThat(result.get(0).getQuantity()).isEqualTo(this.orderItem.getQuantity());
	}

	@Test
	void findAllByOrderId() {
		// given
		this.orderItemRepository.save(this.orderItem);

		// when
		var result = this.orderItemRepository.findAllByOrderId(this.orderId);

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0)).isNotNull();
		assertThat(result.get(0).getOrderId()).isEqualTo(this.orderId);
		assertThat(result.get(0).getProductId()).isEqualTo(this.orderItem.getProductId());
		assertThat(result.get(0).getPrice()).isEqualTo(this.orderItem.getPrice());
		assertThat(result.get(0).getQuantity()).isEqualTo(this.orderItem.getQuantity());
	}

}
