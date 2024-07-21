package com.weareblox.assessment.coin.api.command;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class RemoveCoinCommand {

	@TargetAggregateIdentifier
	private String coinid;

	private Integer quantity;

	private String orderId;

	private boolean isRevert;

}
