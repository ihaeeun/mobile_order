package com.kakaobank.order.common.exception;

public final class MessageBuilder {
	private MessageBuilder() {
	}

	public static String buildOutOfStockMessage(String productName, int stock) {
		return new StringBuilder().append(productName)
				.append(" is out of stock. (Available: ")
				.append(stock)
				.append(")")
				.toString();
	}

}
