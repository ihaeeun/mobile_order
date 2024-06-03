package com.kakaobank.order.common.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_form", indexes = { @Index(name = "idx_userId", columnList = "user_id"),
		@Index(name = "ix_id_orderStatus", columnList = "id, order_status") })
public class Order {

	@Id
	private String id;

	private String userId;

	private OrderStatus orderStatus = OrderStatus.ORDERED;

	private long totalAmount;

	private ZonedDateTime orderDateTime = ZonedDateTime.now();

	public Order() {
	}

	public Order(String id, String userId, int totalAmount) {
		this.id = id;
		this.userId = userId;
		this.totalAmount = totalAmount;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public OrderStatus getOrderStatus() {
		return orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ZonedDateTime getOrderDateTime() {
		return orderDateTime;
	}

	public void setOrderDateTime(ZonedDateTime orderDateTime) {
		this.orderDateTime = orderDateTime;
	}

}
