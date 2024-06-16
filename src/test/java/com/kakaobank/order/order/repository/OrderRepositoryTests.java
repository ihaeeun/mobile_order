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
		this.order = TestUtils.buildOrder();
	}

	@Test
	void save() {
		// when
		var result = this.orderRepository.save(this.order);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getMemberId()).isEqualTo(this.order.getMemberId());
		assertThat(result.getOrderStatus()).isEqualTo(this.order.getOrderStatus());
		assertThat(result.getTotalAmount()).isEqualTo(this.order.getTotalAmount());
		assertThat(result.getOrderDatetime()).isEqualTo(this.order.getOrderDatetime());
	}

	@Test
	void findAllByUserId() {
		// given
		this.orderRepository.save(this.order);

		// when
		var result = this.orderRepository.findAllByMemberId(this.order.getMemberId());

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getMemberId()).isEqualTo(this.order.getMemberId());
		assertThat(result.get(0).getOrderStatus()).isEqualTo(this.order.getOrderStatus());
		assertThat(result.get(0).getTotalAmount()).isEqualTo(this.order.getTotalAmount());
		assertThat(result.get(0).getOrderDatetime()).isEqualTo(this.order.getOrderDatetime());

	}

	@Test
	void findByIdAndOrderStatus() {
		// given
		var id = this.order.getId();
		var status = this.order.getOrderStatus();
		this.orderRepository.save(this.order);

		// when
		var result = this.orderRepository.findByIdAndOrderStatus(id, status);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getMemberId()).isEqualTo(this.order.getMemberId());
		assertThat(result.getOrderStatus()).isEqualTo(this.order.getOrderStatus());
		assertThat(result.getTotalAmount()).isEqualTo(this.order.getTotalAmount());
		assertThat(result.getOrderDatetime()).isEqualTo(this.order.getOrderDatetime());

	}

}
