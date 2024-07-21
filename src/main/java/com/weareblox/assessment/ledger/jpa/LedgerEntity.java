package com.weareblox.assessment.ledger.jpa;


import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.weareblox.assessment.ledger.api.command.TransactionType;

import lombok.Data;

/**
 * This is the ledger entity stored in the database after a success full transaction
 */
@Entity
@Table(name = "ledger")
@Data
@EntityListeners(AuditingEntityListener.class)
public class LedgerEntity {
	
    @Id
    private String id;
    
    /**
     * Type of the transaction - BUY or SELL
     */
    private TransactionType type;
  
    /**
     * opening balance before the transaction
     */
    private BigDecimal openingBalance;
    
    /**
     * amount payed for the transaction
     */
    private BigDecimal amount;
    
    /**
     * final balance after the transaction
     */
    private BigDecimal closingBalance;
           
    /**
     * Coins present with the customer after transaction
     */
    private int coinBalance;
    
    /**
     * Coin used for transaction
     */
    private String coinId;
    
    /**
     * order id that was used to start this transaction
     */
    private String orderId;
    
    /**
     * customer for whom the transaction was done
     */
    private String customerId;
    
    @CreatedDate
    private Date createdDate;
}
