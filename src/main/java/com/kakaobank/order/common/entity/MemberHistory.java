package com.kakaobank.order.common.entity;

import java.time.ZonedDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class MemberHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Enumerated(EnumType.STRING)
	private ActionType actionType;

	@Column(columnDefinition = "TEXT")
	private String request;

	@Column(columnDefinition = "TEXT")
	private String result;

	private ZonedDateTime actionDatetime = ZonedDateTime.now();

	public MemberHistory() {
	}

	public MemberHistory(long id, ActionType actionType, String request, String result,
			ZonedDateTime actionDatetime) {
		this.id = id;
		this.actionType = actionType;
		this.request = request;
		this.result = result;
		this.actionDatetime = actionDatetime;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private long id;

		private ActionType actionType;

		private String request;

		private String result;

		private ZonedDateTime actionDateTime;

		private Builder() {
		}

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder actionType(ActionType actionType) {
			this.actionType = actionType;
			return this;
		}

		public Builder request(String request) {
			this.request = request;
			return this;
		}

		public Builder result(String result) {
			this.result = result;
			return this;
		}

		public Builder actionDateTime(ZonedDateTime actionDateTime) {
			this.actionDateTime = actionDateTime;
			return this;
		}

		public MemberHistory build() {
			return new MemberHistory(this.id, this.actionType, this.request, this.result,
					this.actionDateTime);
		}
	}

}
