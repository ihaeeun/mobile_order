package com.kakaobank.order;

import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.UUID;

import com.kakaobank.order.common.entity.CartItem;
import com.kakaobank.order.common.entity.Gender;
import com.kakaobank.order.common.entity.Member;
import com.kakaobank.order.common.entity.Order;
import com.kakaobank.order.common.entity.OrderItem;
import com.kakaobank.order.common.entity.OrderStatus;
import com.kakaobank.order.common.entity.Payment;
import com.kakaobank.order.common.entity.Product;
import com.kakaobank.order.common.util.UserContext;
import com.kakaobank.order.order.dto.CartItemInfo;

import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.projection.SpelAwareProxyProjectionFactory;

public final class TestUtils {

	public static final String USER_ID = "testUser";

	public static final int QUANTITY = 10;

	private TestUtils() {
	}

	public static Order buildOrder() {
		var order = new Order();
		order.setId(UUID.randomUUID().toString());
		order.setUserId(USER_ID);
		order.setTotalAmount(20000);
		order.setOrderStatus(OrderStatus.PAID);
		order.setOrderDatetime(ZonedDateTime.now());
		return order;
	}

	public static OrderItem buildOrderItem(String orderId) {
		var orderItem = new OrderItem();
		orderItem.setId(1);
		orderItem.setOrderId(orderId);
		orderItem.setQuantity(QUANTITY);
		orderItem.setPrice(2000);
		orderItem.setProductId(1);
		return orderItem;
	}

	public static CartItem buildCartItem() {
		var cartItem = new CartItem();
		cartItem.setId(1);
		cartItem.setUserId(USER_ID);
		cartItem.setProductId(1);
		cartItem.setQuantity(QUANTITY);
		return cartItem;
	}

	public static Product buildProduct() {
		var product = new Product();
		product.setId(1);
		product.setPrice(2000);
		product.setStock(20);
		product.setName("product1");
		return product;
	}

	public static Payment buildPayment() {
		var payment = new Payment();
		payment.setId(1);
		payment.setOrderId(UUID.randomUUID().toString());
		payment.setCancellation(false);
		return payment;
	}

	public static Member buildMember() {
		var member = new Member();
		member.setId(UUID.randomUUID().toString());
		member.setUserId(USER_ID);
		member.setPassword("secret");
		member.setUserName(USER_ID);
		member.setPhone("01011112222");
		member.setBirth(LocalDate.of(2000, 01, 01));
		member.setGender(Gender.MALE);
		return member;
	}

	public static CartItemInfo buildDefaultCartItemInfo() {

		CartItemInfo cartItemInfo = new CartItemInfo() {
			@Override
			public long getId() {
				return 1;
			}

			@Override
			public long getProductId() {
				return 1;
			}

			@Override
			public String getProductName() {
				return "product1";
			}

			@Override
			public int getPrice() {
				return 2000;
			}

			@Override
			public int getQuantity() {
				return QUANTITY;
			}

			@Override
			public int getStock() {
				return 20;
			}
		};

		return cartItemInfo;
	}

	public static CartItemInfo buildCustomCartItemInfo(int quantity) {
		ProjectionFactory projectionFactory = new SpelAwareProxyProjectionFactory();
		Map<String, Object> fields = Map.of(
				"id", 1,
				"productId", 1,
				"productName", "product1",
				"price", 2000,
				"quantity", quantity,
				"stock", 20
		);
		return projectionFactory.createProjection(CartItemInfo.class, fields);
	}

	public static UserContext getTestContext() {
		return new UserContext(UUID.randomUUID().toString(), USER_ID, "/reqPath");
	}

}
