package com.kakaobank.order.common.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(indexes = {
		@Index(name = "ix_memberId", columnList = "member_id"),
		@Index(name = "ix_memberId_productId", columnList = "member_id, product_id")
})
public class CartItem {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String memberId;

	private long productId;

	private int quantity;

	public CartItem() {
	}

	public CartItem(String memberId, long productId, int quantity) {
		this.memberId = memberId;
		this.productId = productId;
		this.quantity = quantity;
	}

	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getMemberId() {
		return this.memberId;
	}

	public void setMemberId(String userId) {
		this.memberId = userId;
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

}
