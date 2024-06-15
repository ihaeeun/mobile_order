package com.kakaobank.order.order.repository;

import java.util.List;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
@DataJpaTest
class CartItemRepositoryTests {

	@Autowired
	CartItemRepository cartItemRepository;

	private CartItem cartItem;

	@BeforeEach
	void setUp() {
		this.cartItem = TestUtils.buildCartItem();
	}

	@Test
	void save() {
		// when
		var result = this.cartItemRepository.save(this.cartItem);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getMemberId()).isEqualTo(this.cartItem.getMemberId());
		assertThat(result.getProductId()).isEqualTo(this.cartItem.getProductId());
		assertThat(result.getQuantity()).isEqualTo(this.cartItem.getQuantity());
	}

	@Test
	void findByUserId() {
		// given
		this.cartItemRepository.save(this.cartItem);

		// when
		var result = this.cartItemRepository.findByUserId(TestUtils.USER_ID);

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getProductId()).isEqualTo(this.cartItem.getProductId());
		assertThat(result.get(0).getQuantity()).isEqualTo(this.cartItem.getQuantity());
	}

	@Test
	void findByUserIdAndProductId() {
		// given
		this.cartItemRepository.save(this.cartItem);

		// when
		var result = this.cartItemRepository.findByUserIdAndProductId(TestUtils.USER_ID, this.cartItem.getProductId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.getMemberId()).isEqualTo(this.cartItem.getMemberId());
		assertThat(result.getProductId()).isEqualTo(this.cartItem.getProductId());
		assertThat(result.getQuantity()).isEqualTo(this.cartItem.getQuantity());
	}

	@Test
	void deleteAllById() {
		// given
		var saved = this.cartItemRepository.save(this.cartItem);

		// when
		this.cartItemRepository.deleteAllById(List.of(saved.getId()));
		var result = this.cartItemRepository.findById(saved.getId());

		// then
		assertThat(result).isEmpty();
	}

}
