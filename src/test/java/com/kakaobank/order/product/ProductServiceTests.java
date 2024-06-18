package com.kakaobank.order.product;

import java.util.List;
import java.util.Optional;

import com.kakaobank.order.TestUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class ProductServiceTests {

	@InjectMocks
	ProductService productService;

	@Mock
	ProductRepository productRepository;

	@Test
	void getProducts() {
		// given
		var product = TestUtils.buildProduct();
		var productList = List.of(product);
		given(this.productRepository.findAll()).willReturn(productList);

		// when
		var result = this.productService.getProducts();

		// then
		assertThat(result.products()).isNotNull().hasSize(1);
		assertThat(result.products().get(0).getName()).isEqualTo(product.getName());
		assertThat(result.products().get(0).getPrice()).isEqualTo(product.getPrice());
		assertThat(result.products().get(0).getStock()).isEqualTo(product.getStock());
	}

	@Test
	void getProductDetail() {
		// given
		var product = TestUtils.buildProduct();
		given(this.productRepository.findById(any())).willReturn(Optional.of(product));

		// when
		var result = this.productService.getProduct(1);

		// then
		assertThat(result).isNotNull();
		assertThat(result.getId()).isEqualTo(product.getId());
		assertThat(result.getName()).isEqualTo(product.getName());
		assertThat(result.getPrice()).isEqualTo(product.getPrice());
		assertThat(result.getStock()).isEqualTo(product.getStock());
	}

	@Test
	void updateStock() {
		// given
		var product = TestUtils.buildProduct();

		// when
		this.productService.updateStock(product.getId(), 4);

		// then
		verify(this.productRepository).updateStock(product.getId(), 4);
	}

}
