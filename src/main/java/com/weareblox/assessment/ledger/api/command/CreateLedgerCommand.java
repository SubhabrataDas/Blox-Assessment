package com.weareblox.assessment.ledger.api.command;


import lombok.Value;

import java.math.BigDecimal;

import org.axonframework.modelling.command.TargetAggregateIdentifier;

/**
 * This is the command to create a Ledger record for the Transaction.
 */
@Value
public class CreateLedgerCommand {
    @TargetAggregateIdentifier
    String ledgerId;
    
    private String coinId;
    
    private BigDecimal payment;
    
    private Integer amount;
    
    private TransactionType type;
    
    private BigDecimal commision;
  
    private String orderId;
}

