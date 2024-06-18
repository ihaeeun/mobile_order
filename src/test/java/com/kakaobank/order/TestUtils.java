package com.kakaobank.order;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;
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

public final class TestUtils {
	public static final String USER_ID = "testUser";

	public static final int QUANTITY = 10;

	private TestUtils() {
	}

	public static Order buildOrder() {
		var order = new Order();
		order.setId(UUID.randomUUID().toString());
		order.setMemberId(USER_ID);
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
		orderItem.setPrice(BigDecimal.valueOf(2000));
		orderItem.setProductId(1);
		return orderItem;
	}

	public static CartItem buildCartItem() {
		var cartItem = new CartItem();
		cartItem.setId(1);
		cartItem.setMemberId(UUID.randomUUID().toString());
		cartItem.setProductId(1);
		cartItem.setQuantity(QUANTITY);
		return cartItem;
	}

	public static Product buildProduct() {
		var product = new Product();
		product.setId(1);
		product.setPrice(BigDecimal.valueOf(2000));
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
		member.setUuid(UUID.randomUUID().toString());
		member.setUserId(USER_ID);
		member.setPassword("secret");
		member.setUserName(USER_ID);
		member.setPhone("01011112222");
		member.setBirth(LocalDate.of(2000, 01, 01));
		member.setGender(Gender.MALE);
		return member;
	}

	public static CartItemInfo buildDefaultCartItemInfo() {
		return new CartItemInfo(1, 1, "product1", BigDecimal.valueOf(2000), QUANTITY, 20);
	}

	public static CartItemInfo buildCustomCartItemInfo(int quantity) {
		return new CartItemInfo(1, 1, "product1", BigDecimal.valueOf(2000), quantity, 20);
	}

	public static UserContext getTestContext() {
		return new UserContext(UUID.randomUUID().toString(), USER_ID, "/reqPath");
	}

}
