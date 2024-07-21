package com.weareblox.assessment.order.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;


@Value
public class CreateOrderCommand {
	
	@TargetAggregateIdentifier
	private String orderId;
	
	private String coinId;
	
	private int quantity;
	
	private String type;
	
	private String customerid;
	
	private String status;

}
