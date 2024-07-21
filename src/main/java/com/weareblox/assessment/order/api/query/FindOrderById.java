package com.weareblox.assessment.order.api.query;

import lombok.Value;

/**
 * this the query used to find an order by id
 */
@Value
public class FindOrderById {
   
	/**
	 * id of the order
	 */
	private String orderId;
	
}
