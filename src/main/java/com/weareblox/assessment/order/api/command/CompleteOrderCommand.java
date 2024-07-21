package com.weareblox.assessment.order.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Builder;
import lombok.Value;


@Value
public class CompleteOrderCommand {

	@TargetAggregateIdentifier
	private String orderId;
	
	
	
}
