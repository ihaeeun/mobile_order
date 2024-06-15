package com.kakaobank.order.order;

import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import com.kakaobank.order.common.aop.UserAction;
import com.kakaobank.order.common.entity.ActionType;
import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.OrderItem;
import com.kakaobank.order.common.entity.OrderStatus;
import com.kakaobank.order.common.exception.MessageBuilder;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.order.dto.CartItemInfo;
import com.kakaobank.order.order.dto.DeleteCartItemRequest;
import com.kakaobank.order.order.dto.OrderEntries;
import com.kakaobank.order.order.dto.OrderRequest;
import com.kakaobank.order.order.repository.OrderItemRepository;
import com.kakaobank.order.order.repository.OrderRepository;
import com.kakaobank.order.payment.PaymentService;
import com.kakaobank.order.payment.dto.PaymentResponse;
import com.kakaobank.order.product.ProductService;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.server.ResponseStatusException;

@Service
public class OrderService {

	private final CartService cartService;

	private final PaymentService paymentService;

	private final ProductService productService;

	private final OrderRepository orderRepository;

	private final OrderItemRepository orderItemRepository;

	OrderService(CartService cartService, OrderRepository orderRepository, OrderItemRepository orderItemRepository,
			PaymentService paymentService, ProductService productService) {
		this.cartService = cartService;
		this.orderRepository = orderRepository;
		this.orderItemRepository = orderItemRepository;
		this.paymentService = paymentService;
		this.productService = productService;
	}

	public OrderEntries getOrderHistory(String userId) {
		return new OrderEntries(this.orderRepository.findAllByUserId(userId));
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@UserAction(actionType = ActionType.MAKE_ORDER)
	public Order makeOrder(UserContext context, OrderRequest requests) {
		// 카트에 담겨 있는 내역만 주문 가능
		var cartItemInfos = this.cartService.getCartItemInfos(requests.cartItemIds());
		if (cartItemInfos.isEmpty()) {
			throw new OrderServiceException(HttpStatus.BAD_REQUEST, "Empty cart");
		}

		var orderId = UUID.randomUUID().toString();
		List<OrderItem> orderItems = new LinkedList<>();

		for (CartItemInfo itemInfo : cartItemInfos) {
			// 재고가 없는 상품이 포함되어 있으면 전체 주문 불가
			if (isInStock(itemInfo)) {
				var orderItem = OrderItem.of(orderId, itemInfo);
				orderItems.add(orderItem);
				this.productService.updateStock(itemInfo.getProductId(), itemInfo.getQuantity());
			}
			else {
				var message = MessageBuilder.buildOutOfStockMessage(itemInfo.getProductName(), itemInfo.getStock());
				throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, message);
			}
		}

		var totalAmount = orderItems.stream().mapToInt((item) -> item.getPrice() * item.getQuantity()).sum();
		var order = new Order(orderId, context.getUuid(), totalAmount);

		try {
			// payment 호출
			var paymentResponse = this.paymentService.makePayment(order);
			return processAfterPayment(context, paymentResponse, order, orderItems, requests.cartItemIds());
		}
		catch (HttpStatusCodeException | InterruptedException ex) {
			throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment.");
		}
	}

	private boolean isInStock(CartItemInfo cartItemInfo) {
		return cartItemInfo.getStock() > cartItemInfo.getQuantity();
	}

	private Order processAfterPayment(UserContext context, PaymentResponse paymentResponse, Order order, List<OrderItem> orderItems, List<Long> cartItemIds) {
		if (paymentResponse.result()) {
			this.orderItemRepository.saveAll(orderItems);

			// 결제 완료 후, 구매한 상품은 카트에서 제거
			this.cartService.deleteCartItems(context, new DeleteCartItemRequest(cartItemIds));

			// 주문 상태 업데이트
			order.setOrderStatus(OrderStatus.PAID);
			return this.orderRepository.save(order);
		}
		throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail payment.");
	}

	@Transactional(isolation = Isolation.READ_COMMITTED)
	@UserAction(actionType = ActionType.CANCEL_ORDER)
	public Order cancelOrder(UserContext userContext, String orderId) {
		var order = this.orderRepository.findByIdAndOrderStatus(orderId, OrderStatus.PAID);
		if (ObjectUtils.isEmpty(order)) {
			throw new OrderServiceException(HttpStatus.BAD_REQUEST, "It is not valid order.");
		}

		try {
			var paymentResponse = this.paymentService.cancelPayment(order);

			if (paymentResponse.result()) {
				// 상품 재고 업데이트
				this.orderItemRepository.findAllByOrderId(orderId).forEach((item) -> this.productService.updateStock(item.getProductId(), item.getQuantity() * -1));

				order.setOrderStatus(OrderStatus.CANCELED);
				return this.orderRepository.save(order);
			}
			throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail cancel payment.");
		}
		catch (HttpStatusCodeException | InterruptedException ex) {
			throw new OrderServiceException(HttpStatus.INTERNAL_SERVER_ERROR, "Fail cancel payment.");
		}
	}


	public static class OrderServiceException extends ResponseStatusException {

		public OrderServiceException(HttpStatusCode statusCode, String reason) {
			super(statusCode, reason);
		}

	}

}
