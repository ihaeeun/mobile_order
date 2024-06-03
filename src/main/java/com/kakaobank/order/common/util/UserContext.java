package com.kakaobank.order.common.util;

public class UserContext {

	private String uuid;

	private String userId;

	private String reqPath;

	public UserContext() {
	}

	public UserContext(String uuid, String userId, String reqPath) {
		this.uuid = uuid;
		this.userId = userId;
		this.reqPath = reqPath;
	}

	public String getUuid() {
		return this.uuid;
	}

	public String getUserId() {
		return this.userId;
	}

	public String getReqPath() {
		return this.reqPath;
	}
}
