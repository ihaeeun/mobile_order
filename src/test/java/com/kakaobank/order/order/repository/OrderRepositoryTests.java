package com.kakaobank.order.order.repository;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.Order;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class OrderRepositoryTests {

	@Autowired
	OrderRepository orderRepository;

	private Order order;

	@BeforeEach
	void setUp() {
		order = TestUtils.buildOrder();
	}

	@Test
	void save() {
		// when
		var result = orderRepository.save(order);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(order.getUserId());
		assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
		assertThat(result.getTotalAmount()).isEqualTo(order.getTotalAmount());
		assertThat(result.getOrderDatetime()).isEqualTo(order.getOrderDatetime());
	}

	@Test
	void findAllByUserId() {
		// given
		orderRepository.save(order);
		var userId = TestUtils.USER_ID;

		// when
		var result = this.orderRepository.findAllByUserId(userId);

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getUserId()).isEqualTo(order.getUserId());
		assertThat(result.get(0).getOrderStatus()).isEqualTo(order.getOrderStatus());
		assertThat(result.get(0).getTotalAmount()).isEqualTo(order.getTotalAmount());
		assertThat(result.get(0).getOrderDatetime()).isEqualTo(order.getOrderDatetime());

	}

	@Test
	void findByIdAndOrderStatus() {
		// given
		var id = order.getId();
		var status = order.getOrderStatus();
		orderRepository.save(order);

		// when
		var result = this.orderRepository.findByIdAndOrderStatus(id, status);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(order.getUserId());
		assertThat(result.getOrderStatus()).isEqualTo(order.getOrderStatus());
		assertThat(result.getTotalAmount()).isEqualTo(order.getTotalAmount());
		assertThat(result.getOrderDatetime()).isEqualTo(order.getOrderDatetime());

	}

}