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
public class UserAction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String userId;

	@Enumerated(EnumType.STRING)
	private ActionType actionType;

	private String apiPath;

	@Column(columnDefinition = "TEXT")
	private String request;

	@Column(columnDefinition = "TEXT")
	private String result;

	private ZonedDateTime actionDatetime;

	public UserAction() {
	}

	public UserAction(long id, String userId, ActionType actionType, String apiPath, String request, String result,
			ZonedDateTime actionDatetime) {
		this.id = id;
		this.userId = userId;
		this.actionType = actionType;
		this.apiPath = apiPath;
		this.request = request;
		this.result = result;
		this.actionDatetime = actionDatetime;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public void setActionType(ActionType actionType) {
		this.actionType = actionType;
	}

	public void setApiPath(String apiPath) {
		this.apiPath = apiPath;
	}

	public void setRequest(String request) {
		this.request = request;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public void setActionDatetime(ZonedDateTime actionDateTime) {
		this.actionDatetime = actionDateTime;
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private long id;

		private String userId;

		private ActionType actionType;

		private String apiPath;

		private String request;

		private String result;

		private ZonedDateTime actionDateTime;

		private Builder() {
		}

		public Builder id(long id) {
			this.id = id;
			return this;
		}

		public Builder userId(String userId) {
			this.userId = userId;
			return this;
		}

		public Builder actionType(ActionType actionType) {
			this.actionType = actionType;
			return this;
		}

		public Builder apiPath(String apiPath) {
			this.apiPath = apiPath;
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

		public UserAction build() {
			return new UserAction(this.id, this.userId, this.actionType, this.apiPath, this.request, this.result, this.actionDateTime);
		}
	}
}
