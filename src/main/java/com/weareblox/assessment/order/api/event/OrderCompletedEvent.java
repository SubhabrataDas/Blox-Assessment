package com.weareblox.assessment.order.api.event;

import lombok.Value;

/**
 * Event for successful Order completing
 */
@Value
public class OrderCompletedEvent {
	
	/**
	 * id of the order
	 */
	private String orderId;

}
