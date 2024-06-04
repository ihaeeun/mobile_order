package com.kakaobank.order.product;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTests {

	@Autowired
	ProductRepository productRepository;

	private Product product;

	@BeforeEach
	void setUp() {
		product = TestUtils.buildProduct();
	}

	@Test
	void saveAll() {
		// when
		var result = this.productRepository.saveAll(List.of(product));

		// then
		assertThat(result).isNotNull();
		assertThat(result.get(0).getStock()).isEqualTo(product.getStock());
	}

	@Test
	void findAll() {
		// given
		var saved = this.productRepository.save(product);

		// when
		var result = this.productRepository.findAll();

		// then
		assertThat(result).isNotNull();
		assertThat(result.get(0).getName()).isEqualTo(saved.getName());
		assertThat(result.get(0).getPrice()).isEqualTo(saved.getPrice());
		assertThat(result.get(0).getStock()).isEqualTo(saved.getStock());
	}

	@Test
	void findById() {
		// given
		var saved = this.productRepository.save(product);

		// when
		var result = this.productRepository.findById(saved.getId());

		// then
		assertThat(result).isNotNull();
		assertThat(result.get().getName()).isEqualTo(saved.getName());
		assertThat(result.get().getPrice()).isEqualTo(saved.getPrice());
		assertThat(result.get().getStock()).isEqualTo(saved.getStock());
	}

}