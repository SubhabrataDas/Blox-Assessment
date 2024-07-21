package com.weareblox.assessment.order.api.event;

import lombok.Value;

/**
 * Event for unsuccessful Order
**/

@Value
public class OrderStoppedEvent {

	/**
	 * id of the order
	 */
	private String orderId;	
}
