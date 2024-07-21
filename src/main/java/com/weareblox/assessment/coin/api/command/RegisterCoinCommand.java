package com.weareblox.assessment.coin.api.command;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class RegisterCoinCommand {

	@TargetAggregateIdentifier
	String coinid;

	private String name;

	private String logoUrl;

	private String description;

	private Integer balance;

	private BigDecimal price;
	
	private BigDecimal commision;
	
}
