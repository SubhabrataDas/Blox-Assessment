package com.weareblox.assessment.coin.api.command;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

import lombok.Value;

@Value
public class UpdateCoinPriceCommand {
	
	 @TargetAggregateIdentifier
	   String coinid;
	  
	   BigDecimal price;

}

