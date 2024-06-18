package com.kakaobank.order.order;

import java.util.List;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.repository.CartItemRepository;
import com.kakaobank.order.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class CartServiceTests {

	@InjectMocks
	CartService cartService;

	@Mock
	ProductService productService;

	@Mock
	CartItemRepository cartItemRepository;

	private final String USER_ID = "testuser";

	@Test
	void addCart_emptyCart() {
		// given
		var product = TestUtils.buildProduct();
		given(this.productService.getProduct(product.getId())).willReturn(product);

		var context = TestUtils.getTestContext();
		given(this.cartItemRepository.findByMemberIdAndProductId(context.getUuid(), product.getId())).willReturn(null);

		var cartItemInfo = TestUtils.buildDefaultCartItemInfo();
		given(this.cartItemRepository.findCartItemResponseByMemberId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var cartItem = TestUtils.buildCartItem();
		var request = new AddCartRequest(cartItem.getProductId(), cartItem.getQuantity());
		var result = this.cartService.addCart(context, request);

		// then
		verify(this.cartItemRepository).save(any());
		assertThat(result).isNotNull();
		assertThat(result.cartResponses()).isNotNull().hasSize(1);
		var cartResponse = result.cartResponses().get(0);
		assertThat(cartResponse).isNotNull();
		assertThat(cartResponse.productId()).isEqualTo(cartItem.getProductId());
		assertThat(cartResponse.price()).isEqualTo(product.getPrice());
		assertThat(cartResponse.quantity()).isEqualTo(cartItem.getQuantity());
	}

	@Test
	void addCart_notEmptyCart() {
		// given
		var product = TestUtils.buildProduct();
		given(this.productService.getProduct(product.getId())).willReturn(product);

		var context = TestUtils.getTestContext();
		var cartItem = TestUtils.buildCartItem();
		given(this.cartItemRepository.findByMemberIdAndProductId(context.getUuid(), product.getId())).willReturn(cartItem);

		var quantity = 5;
		var cartItemInfo = TestUtils.buildCustomCartItemInfo(cartItem.getQuantity() + quantity);
		given(this.cartItemRepository.findCartItemResponseByMemberId(context.getUuid()))
				.willReturn(List.of(cartItemInfo));

		// when
		var request = new AddCartRequest(cartItem.getProductId(), 5);
		var result = this.cartService.addCart(context, request);

		// then
		verify(this.cartItemRepository).save(any());
		assertThat(result).isNotNull();
		assertThat(result.cartResponses()).isNotNull().hasSize(1);
		var cartResponse = result.cartResponses().get(0);
		assertThat(cartResponse).isNotNull();
		assertThat(cartResponse.productId()).isEqualTo(cartItem.getProductId());
		assertThat(cartResponse.price()).isEqualTo(product.getPrice());
		assertThat(cartResponse.quantity()).isEqualTo(cartItem.getQuantity());
	}

	@Test
	void addCart_unavailableProduct() {
		// given
		var product = TestUtils.buildProduct();
		product.setStock(0);
		given(this.productService.getProduct(product.getId())).willReturn(product);

		var cartItem = TestUtils.buildCartItem();
		var request = new AddCartRequest(cartItem.getProductId(), cartItem.getQuantity());
		var context = TestUtils.getTestContext();

		// when, then
		assertThatThrownBy(() -> this.cartService.addCart(context, request))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("out of stock");
	}

	@Test
	void getCartList() {
		// given
		var cartItemInfo = TestUtils.buildDefaultCartItemInfo();
		given(this.cartItemRepository.findCartItemResponseByMemberId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var result = this.cartService.getCartList(this.USER_ID);

		// then
		assertThat(result).isNotNull();
		assertThat(result.cartResponses()).isNotNull().hasSize(1);
		var cartResponse = result.cartResponses().get(0);
		assertThat(cartResponse).isNotNull();
		assertThat(cartResponse.productId()).isEqualTo(cartItemInfo.getProductId());
		assertThat(cartResponse.price()).isEqualTo(cartItemInfo.getPrice());
		assertThat(cartResponse.quantity()).isEqualTo(cartItemInfo.getQuantity());
	}

	@Test
	void deleteCartItems() {
		// given
		var cartItemInfo = TestUtils.buildDefaultCartItemInfo();
		given(this.cartItemRepository.findCartItemResponseByMemberId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var context = TestUtils.getTestContext();
		var request = new DeleteCartItemRequest(List.of(2L));
		var result = this.cartService.deleteCartItems(context, request);

		// then
		verify(this.cartItemRepository).deleteAllById(any());
		assertThat(result).isNotNull();
		assertThat(result.cartResponses()).isNotNull().hasSize(1);
		var cartResponse = result.cartResponses().get(0);
		assertThat(cartResponse).isNotNull();
	}

}
