package com.weareblox.assessment.order.dto;

import javax.annotation.Nullable;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import com.weareblox.assessment.controller.validator.ValueOfEnum;
import com.weareblox.assessment.order.jpa.OrderStatus;
import com.weareblox.assessment.order.jpa.OrderType;

import lombok.Builder;
import lombok.Value;


/**
 * This is the Order DTO used for handling request for creating an order and showing
 * the order updates to the client.
 */


@Value
public class Order {
	
	/**
	 * Order id
	 */
	private String id;
	
	/**
	 * id of coin used in the order
	 */
	@NotBlank
	private String coinId;
	
	/**
	 * id of the customer used in the order
	 */
	@NotBlank
	private String customerId;
	
	/**
	 * number of coins to sell or buy
	 */
	@NotNull
	@Min(0)
	private Integer quantity;
	
	/**
	 * type of order - sell or buy
	 */
	@NotBlank
	@ValueOfEnum(enumClass = OrderType.class)
	private String type;
	
	/**
	 * current status of the order
	 */
	@ValueOfEnum(enumClass = OrderStatus.class)
	private String status;
	
	/**
	 * any comment for the order
	 */
	@Nullable
	private String comment;


}
