package com.kakaobank.order.order.repository;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.CartItem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class CartItemRepositoryTests {

	@Autowired
	CartItemRepository cartItemRepository;

	private CartItem cartItem;

	@BeforeEach
	void setUp() {
		cartItem = TestUtils.buildCartItem();
	}

	@Test
	void save() {
		// when
		var result = this.cartItemRepository.save(cartItem);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(cartItem.getUserId());
		assertThat(result.getProductId()).isEqualTo(cartItem.getProductId());
		assertThat(result.getQuantity()).isEqualTo(cartItem.getQuantity());
	}

	@Test
	void findByUserId() {
		// given
		this.cartItemRepository.save(cartItem);

		// when
		var result = this.cartItemRepository.findByUserId(TestUtils.USER_ID);

		// then
		assertThat(result).isNotNull().hasSize(1);
		assertThat(result.get(0).getProductId()).isEqualTo(cartItem.getProductId());
		assertThat(result.get(0).getQuantity()).isEqualTo(cartItem.getQuantity());
	}

	@Test
	void findByUserIdAndProductId() {
		// given
		this.cartItemRepository.save(cartItem);

		// when
		var result = this.cartItemRepository.findByUserIdAndProductId(TestUtils.USER_ID, cartItem.getProductId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.getUserId()).isEqualTo(cartItem.getUserId());
		assertThat(result.getProductId()).isEqualTo(cartItem.getProductId());
		assertThat(result.getQuantity()).isEqualTo(cartItem.getQuantity());
	}

	@Test
	void deleteAllById() {
		// given
		var saved = this.cartItemRepository.save(cartItem);

		// when
		this.cartItemRepository.deleteAllById(List.of(saved.getId()));
		var result = this.cartItemRepository.findById(saved.getId());

		// then
		assertThat(result).isEmpty();
	}

}