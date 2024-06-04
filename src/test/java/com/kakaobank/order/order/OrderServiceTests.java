package com.kakaobank.order.order;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.List;

import com.kakaobank.order.TestUtils;
import com.kakaobank.order.common.entity.OrderStatus;
import com.kakaobank.order.order.dto.AddCartRequest;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.dto.OrderRequest;
import com.kakaobank.order.order.repository.CartItemRepository;
import com.kakaobank.order.order.repository.OrderItemRepository;
import com.kakaobank.order.order.repository.OrderRepository;
import com.kakaobank.order.payment.PaymentService;
import com.kakaobank.order.payment.dto.PaymentResponse;
import com.kakaobank.order.product.ProductService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class OrderServiceTests {

	@InjectMocks
	OrderService orderService;

	@Mock
	OrderRepository orderRepository;

	@Mock
	OrderItemRepository orderItemRepository;

	@Mock
	CartItemRepository cartItemRepository;

	@Mock
	PaymentService paymentService;

	@Mock
	ProductService productService;

	private final String USER_ID = "testuser";

	@Test
	void addCart_emptyCart() {
		// given
		var product = TestUtils.buildProduct();
		given(this.productService.getProductDetail(product.getId())).willReturn(product);

		var context = TestUtils.getTestContext();
		given(this.cartItemRepository.findByUserIdAndProductId(context.getUuid(), product.getId())).willReturn(null);

		var cartItemInfo = TestUtils.buildDefaultCartItemInfo();
		given(this.cartItemRepository.findCartItemInfoByUserId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var cartItem = TestUtils.buildCartItem();
		var request = new AddCartRequest(cartItem.getProductId(), cartItem.getQuantity());
		var result = this.orderService.addCart(context, request);

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
		given(this.productService.getProductDetail(product.getId())).willReturn(product);

		var context = TestUtils.getTestContext();
		var cartItem = TestUtils.buildCartItem();
		given(this.cartItemRepository.findByUserIdAndProductId(context.getUuid(), product.getId())).willReturn(cartItem);

		var quantity = 5;
		var cartItemInfo = TestUtils.buildCustomCartItemInfo(cartItem.getQuantity() + quantity);
		given(this.cartItemRepository.findCartItemInfoByUserId(context.getUuid()))
				.willReturn(List.of(cartItemInfo));

		// when
		var request = new AddCartRequest(cartItem.getProductId(), 5);
		var result = this.orderService.addCart(context, request);

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
		given(this.productService.getProductDetail(product.getId())).willReturn(product);

		var cartItem = TestUtils.buildCartItem();
		var request = new AddCartRequest(cartItem.getProductId(), cartItem.getQuantity());
		var context = TestUtils.getTestContext();

		// when, then
		assertThatThrownBy(() -> this.orderService.addCart(context, request))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("out of stock");
	}

	@Test
	void getCartList() {
		// given
		var cartItemInfo = TestUtils.buildDefaultCartItemInfo();
		given(this.cartItemRepository.findCartItemInfoByUserId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var result = this.orderService.getCartList(this.USER_ID);

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
		given(this.cartItemRepository.findCartItemInfoByUserId(any()))
				.willReturn(List.of(cartItemInfo));

		// when
		var context = TestUtils.getTestContext();
		var request = new DeleteCartItemRequest(List.of(2L));
		var result = this.orderService.deleteCartItems(context, request);

		// then
		verify(this.cartItemRepository).deleteAllById(any());
		assertThat(result).isNotNull();
		assertThat(result.cartResponses()).isNotNull().hasSize(1);
		var cartResponse = result.cartResponses().get(0);
		assertThat(cartResponse).isNotNull();

	}

	@Test
	void getOrderHistory() {
		// given
		var order = TestUtils.buildOrder();
		var userId = this.USER_ID;
		given(this.orderRepository.findAllByUserId(any())).willReturn(List.of(order));

		// when
		var result = this.orderService.getOrderHistory(userId);

		// then
		assertThat(result).isNotNull();
		assertThat(result.orderHistory()).isNotNull().hasSize(1);
		var orderHistory = result.orderHistory().get(0);
		assertThat(orderHistory).isNotNull();
		assertThat(orderHistory.getOrderStatus()).isEqualTo(OrderStatus.PAID);
		assertThat(orderHistory.getOrderDatetime()).isBefore(ZonedDateTime.now());
		assertThat(orderHistory.getTotalAmount()).isEqualTo(order.getTotalAmount());
		assertThat(orderHistory.getUserId()).isEqualTo(order.getUserId());
	}

	@Test
	void makeOrder_emptyCart() {
		// given
		given(this.cartItemRepository.findAllCartItemInfoById(any())).willReturn(Collections.emptyList());
		var context = TestUtils.getTestContext();
		var request = new OrderRequest(List.of(1L));

		// when, then
		assertThatThrownBy(() -> this.orderService.makeOrder(context, request))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("Empty cart");
	}

	@Test
	void makeOrder_outOfStock() {
		// given
		var cartItemInfos = List.of(TestUtils.buildCustomCartItemInfo(30));
		given(this.cartItemRepository.findAllCartItemInfoById(any())).willReturn(cartItemInfos);

		// when, then
		var context = TestUtils.getTestContext();
		var request = new OrderRequest(List.of(1L));
		assertThatThrownBy(() -> this.orderService.makeOrder(context, request))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("out of stock");
	}

	@Test
	void makeOrder_successPayment() throws InterruptedException {
		// given
		var cartItemInfos = List.of(TestUtils.buildDefaultCartItemInfo());
		given(this.cartItemRepository.findAllCartItemInfoById(any())).willReturn(cartItemInfos);

		var payment = TestUtils.buildPayment();
		var paymentResponse = new PaymentResponse(true, payment);
		given(this.paymentService.makePayment(any())).willReturn(paymentResponse);

		var order = TestUtils.buildOrder();
		given(this.orderRepository.save(any())).willReturn(order);

		// when
		var context = TestUtils.getTestContext();
		var request = new OrderRequest(List.of(1L));
		var result = this.orderService.makeOrder(context, request);

		// then
		verify(this.productService).updateStock(cartItemInfos.get(0).getProductId(), cartItemInfos.get(0).getQuantity());
		verify(this.orderItemRepository).saveAll(any());
		assertThat(result).isNotNull();
		assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.PAID);
	}

	@Test
	void makeOrder_failPayment() throws InterruptedException {
		// given
		var cartItemInfos = List.of(TestUtils.buildDefaultCartItemInfo());
		given(this.cartItemRepository.findAllCartItemInfoById(any())).willReturn(cartItemInfos);

		given(this.paymentService.makePayment(any()))
				.willThrow(new PaymentService.PaymentServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment."));

		// when, then
		var context = TestUtils.getTestContext();
		var request = new OrderRequest(List.of(1L));
		assertThatThrownBy(() -> this.orderService.makeOrder(context, request))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("Fail payment.");
	}

	@Test
	void cancelOrder_notValidOrder() {
		// given
		given(this.orderRepository.findByIdAndOrderStatus(any(), any())).willReturn(null);

		// when, then
		assertThatThrownBy(() -> this.orderService.cancelOrder(any(), any()))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("not valid order");
	}

	@Test
	void cancelOrder_successCancelPayment() throws Exception {
		// given
		var order = TestUtils.buildOrder();
		given(this.orderRepository.findByIdAndOrderStatus(any(), any())).willReturn(order);

		var payment = TestUtils.buildPayment();
		payment.setCancellation(true);
		var paymentResponse = new PaymentResponse(true, payment);
		given(this.paymentService.cancelPayment(any())).willReturn(paymentResponse);

		var orderItem = TestUtils.buildOrderItem(order.getId());
		given(this.orderItemRepository.findAllByOrderId(any())).willReturn(List.of(orderItem));

		given(this.orderRepository.save(any())).willReturn(order);

		// when
		var context = TestUtils.getTestContext();
		var result = this.orderService.cancelOrder(context, order.getId());

		// then
		verify(this.orderItemRepository).findAllByOrderId(any());
		verify(this.productService).updateStock(orderItem.getProductId(), orderItem.getQuantity() * -1);
		assertThat(result).isNotNull();
		assertThat(result.getOrderStatus()).isEqualTo(OrderStatus.CANCELED);
	}

	@Test
	void cancelOrder_failCancelPayment() throws Exception {
		// given
		var order = TestUtils.buildOrder();
		given(this.orderRepository.findByIdAndOrderStatus(any(), any())).willReturn(order);

		var payment = TestUtils.buildPayment();
		payment.setCancellation(true);
		given(this.paymentService.cancelPayment(any())).willThrow(
				new PaymentService.PaymentServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail cancel payment."));

		// when, then
		assertThatThrownBy(() -> this.orderService.cancelOrder(any(), any()))
				.isInstanceOf(OrderService.OrderServiceException.class)
				.hasMessageContaining("Fail cancel payment.");

	}
}
