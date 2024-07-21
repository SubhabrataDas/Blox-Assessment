package com.weareblox.assessment.order.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class StopOrderCommand {

	@TargetAggregateIdentifier
	private String orderId;
	
}
