package com.weareblox.assessment.ledger.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateCreationPolicy;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.CreationPolicy;
import org.axonframework.spring.stereotype.Aggregate;

import com.weareblox.assessment.ledger.api.command.CreateLedgerCommand;
import com.weareblox.assessment.ledger.api.command.TransactionType;
import com.weareblox.assessment.ledger.api.event.LedgerCreateFailedEvent;
import com.weareblox.assessment.ledger.api.event.LedgerCreatedEvent;

import lombok.extern.slf4j.Slf4j;


/**
 * This is the aggregate for the Ledger. This stores an entry for a successful transaction.
 * After successful creation, the LedgerCreatedEvent is fired.
 * In case of failure, LedgerCreateFailedEvent would be fired.
 */
@Slf4j
@Aggregate
public class LedgerAggregate {

    @AggregateIdentifier
    private String ledgerId;
    
    /**
     * This is the opening balance of the ledger entry. 
     * This is the starting balance in the account with which the transaction was started.
     * Opening balance can be negative, in case more money was paid than earned in the previous transaction.
     */
    private BigDecimal currentBalance;
    
    /**
     * this is the payment done for the transaction
     */
    private BigDecimal payment;
    
    /**
     * this is the commission paid for the transaction.
     */
    private BigDecimal commision;
    
    /**
     * This stores the total number of coins that has been involved in the transactions till now.
     * This is used to validate if the number of coin sold is not more than present in the account.
     */
    private Map<String,Integer> coinStore = new HashMap<>();

    /**
     * This command creates a Ledger entry.
     * This would calculate the closing balance finally left after the transaction, using the following formula
     * In case of
     * 1. BUY (of coins) , the final balance is  (opening balance-payment-commission)
     * 2, SELL ((of coins), the final balance is  (opening balance+payment-commission)
     * @param command
     */
    @CommandHandler
    @CreationPolicy(AggregateCreationPolicy.CREATE_IF_MISSING)
    private void handle(CreateLedgerCommand command) {
    	
    	log.debug("Ledger created command fired", command);
    	
    	this.ledgerId = command.getLedgerId();
    	BigDecimal openingBalance = BigDecimal.ZERO.add(this.currentBalance!=null
    										?this.currentBalance:BigDecimal.ZERO);
    	int currentCoinBalance = this.coinStore.get(command.getCoinId())!=null?
    								this.coinStore.get(command.getCoinId()):0;
    	System.out.println("Leader created command "+ currentCoinBalance);
    	
    	currentCoinBalance = calculateCoinBalance(command.getType(), command.getAmount(), currentCoinBalance);
    	
    	if(currentCoinBalance < 0) {
    		log.debug("Ledger failed command fired", command);
    		apply(LedgerCreateFailedEvent.
    				builder().coinId(command.getCoinId())
    				.orderId(command.getOrderId()).build());
    	}else {
    		log.debug("Ledger created command fired", command);
			apply(LedgerCreatedEvent.builder().
					ledgerId(command.getLedgerId())
					.commision(command.getCommision())
					.closingBalance(calculateClosingBalance(openingBalance,
							command.getPayment(), 
							command.getCommision(),
							command.getType()))
					.openingBalance(openingBalance)
					.currentCoinBalance(currentCoinBalance)
					.orderId(command.getOrderId())
					.payment(command.getPayment())
					.type(command.getType())
					.coinAmount(command.getAmount()).coinId(command.getCoinId()).build());
    	}
    }
    

    /**
     * This is the event generated when the ledger is created.
     * This will eventually save the Ledger entry in Database for querying.
     * @param event LedgerCreatedEvent
     */
    @EventSourcingHandler
    private void on(LedgerCreatedEvent event) {
    	log.debug("Ledger created event fired", event);
        this.ledgerId = event.getLedgerId();
        if(this.currentBalance == null) {
        	this.currentBalance = BigDecimal.ZERO;
        }
        if(this.commision == null) {
        	this.commision = BigDecimal.ZERO;
        }
        if(this.payment == null) {
        	this.payment =  BigDecimal.ZERO;
        }
        Integer coinBalance = coinStore.get(event.getCoinId());
        System.out.println("Leader created event entry "+ coinBalance);
        if(event.getType().equals(TransactionType.BUY)) {   	
        	coinStore.put(event.getCoinId(), (coinBalance==null?0:coinBalance) + event.getCoinAmount()); 	
        	this.currentBalance = this.currentBalance.subtract(event.getPayment().add(event.getCommision()));
        }
        else {
        	coinStore.put(event.getCoinId(), (coinBalance==null?0:coinBalance) - event.getCoinAmount()); 	
        	this.currentBalance = this.currentBalance.add(event.getPayment()).subtract(event.getCommision());
        }
        log.debug("Ledger created event exited", event);
        System.out.println("Leader created event exit "+ coinBalance);
    }
    
    /**
     * this calculates the final coin balance.
     * @param type Transaction Type - buy or sell
     * @param amount how many coins to buy or sell
     * @param currentCoinBalance the number of coins currently owned by the customer.
     * @return
     */
    private int calculateCoinBalance(TransactionType type,int amount,int currentCoinBalance) {
    	if(type.equals(TransactionType.BUY)) {  
    		return currentCoinBalance + amount;
    	}else {
    		return currentCoinBalance - amount;
    	}
    }
    
    /**
     * this calculates the closing balance that is finally left after the transaction
     * @param currentBalance Opening balance before the transaction
     * @param payment payment done for the transaction
     * @param commission commission paid for the transaction
     * @param type type of Transaction (BUY or SELL)
     * @return
     */
	private BigDecimal calculateClosingBalance(BigDecimal currentBalance, BigDecimal payment, BigDecimal commission,
			TransactionType type) {
		BigDecimal currentBalanceInternal = currentBalance != null ? BigDecimal.ZERO.add(currentBalance)
				: BigDecimal.ZERO;
		BigDecimal commissionInternal = commission != null ? BigDecimal.ZERO.add(commission) : BigDecimal.ZERO;
		BigDecimal paymentInternal = payment != null ? BigDecimal.ZERO.add(payment) : BigDecimal.ZERO;

		if (type.equals(TransactionType.BUY)) {
			return currentBalanceInternal.subtract(paymentInternal.add(commissionInternal));
		} else {
			return currentBalanceInternal.add(paymentInternal).subtract(commissionInternal);
		}
	}
    
    public LedgerAggregate() {
    	
    }
}
