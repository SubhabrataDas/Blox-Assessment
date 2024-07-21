package com.weareblox.assessment.order.api.event;

import lombok.Builder;
import lombok.Value;

/**
 * Event for successful Order creation
 */
@Builder
@Value
public class OrderCreatedEvent {
	
	/**
	 * id of the order
	 */
	private String orderId;
	
	/**
	 * Coin for which the order is done.
	 */
	private String coinId;
	
	/**
	 * number of coins to buy/sell
	 */
	private int quantity;
	
	/**
	 * Type of order - buy or sell
	 */
	private String type;
	
	/**
	 * id of customer submitting the order
	 */
	private String customerid;

}
