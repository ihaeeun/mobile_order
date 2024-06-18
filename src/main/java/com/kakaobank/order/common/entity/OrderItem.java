package com.kakaobank.order.common.entity;

import java.math.BigDecimal;

import com.kakaobank.order.order.dto.CartItemInfo;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = @Index(name = "ix_orderId", columnList = "order_id"))
public class OrderItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String orderId;

	private long productId;

	private int quantity;

	private BigDecimal price;

	public OrderItem() {
	}

	private OrderItem(String orderId, long productId, int quantity, BigDecimal price) {
		this.orderId = orderId;
		this.productId = productId;
		this.quantity = quantity;
		this.price = price;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOrderId() {
		return this.orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}

	public long getProductId() {
		return this.productId;
	}

	public void setProductId(long productId) {
		this.productId = productId;
	}

	public int getQuantity() {
		return this.quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public BigDecimal getPrice() {
		return this.price;
	}

	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	public static OrderItem of(String orderId, CartItemInfo itemInfo) {
		return new OrderItem(orderId, itemInfo.getProductId(), itemInfo.getQuantity(), itemInfo.getPrice());
	}
}
