package com.kakaobank.order.common.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "order_form", indexes = {
		@Index(name = "ix_memberId", columnList = "memberId"),
		@Index(name = "ix_id_orderStatus", columnList = "id, order_status")
})
public class Order {

	@Id
	private String id;

	private String memberId;

	private OrderStatus orderStatus = OrderStatus.ORDERED;

	private long totalAmount;

	private ZonedDateTime orderDatetime = ZonedDateTime.now();

	public Order() {
	}

	public Order(String id, String memberId, int totalAmount) {
		this.id = id;
		this.memberId = memberId;
		this.totalAmount = totalAmount;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	public OrderStatus getOrderStatus() {
		return this.orderStatus;
	}

	public void setOrderStatus(OrderStatus orderStatus) {
		this.orderStatus = orderStatus;
	}

	public long getTotalAmount() {
		return this.totalAmount;
	}

	public void setTotalAmount(long totalAmount) {
		this.totalAmount = totalAmount;
	}

	public ZonedDateTime getOrderDatetime() {
		return this.orderDatetime;
	}

	public void setOrderDatetime(ZonedDateTime orderDateTime) {
		this.orderDatetime = orderDateTime;
	}

}
