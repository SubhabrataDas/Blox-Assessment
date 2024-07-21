package com.weareblox.assessment.coin.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.math.BigDecimal;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.weareblox.assessment.coin.api.command.AddCoinCommand;
import com.weareblox.assessment.coin.api.command.RegisterCoinCommand;
import com.weareblox.assessment.coin.api.command.RemoveCoinCommand;
import com.weareblox.assessment.coin.api.command.UpdateCoinPriceCommand;
import com.weareblox.assessment.coin.api.event.CoinAddedEvent;
import com.weareblox.assessment.coin.api.event.CoinPriceUpdatedEvent;
import com.weareblox.assessment.coin.api.event.CoinRegisteredEvent;
import com.weareblox.assessment.coin.api.event.CoinRemovedEvent;

import lombok.extern.slf4j.Slf4j;


/**
 * This is the Aggregate for the Coin.
 * This handles the commands for registering ,updating of price and quantity of the coin.
 */
@Slf4j
@Aggregate
public class CoinAggregate {
	
	@AggregateIdentifier
    private String coinId;
	
	private int balance;
	
	private BigDecimal price;
	
	/**
	 * This command registers a coin in the system
	 * @param command
	 */
	@CommandHandler
    public CoinAggregate(RegisterCoinCommand command) {
		
		log.debug("Register coin command fired",command);
		
		System.out.println("Register command " +this.balance);
			coinId = command.getCoinid();
	        balance = command.getBalance();
	        price = command.getPrice();
	        apply(new CoinRegisteredEvent(command.
	        		getCoinid(),
	        		command.getName(),
	        		command.getDescription(),
	        		command.getLogoUrl(),
	        		command.getBalance(),
	        		command.getPrice(),
	        		command.getCommision()));
    }
	
	/**
	 * This command is used to add a coin to the system
	 * @param command AddCoinCommand
	 */
	@CommandHandler
    private void handle(AddCoinCommand command) {
		log.debug("Add coin command fired",command);
		System.out.println("Add coin command entry " +this.balance);
		coinId = command.getCoinId();
        apply(new CoinAddedEvent(command.getCoinId(),
        		command.getQuantity(),command.getOrderId(),command.isRevert()));
    }
	
	/**
	 * This command is used to remove a coin to the system
	 * @param command RemoveCoinCommand
	 */
	@CommandHandler
	private void handle(RemoveCoinCommand command) {
		log.debug("Remove coin command fired",command);
		System.out.println("Remove coin command entry " + this.balance);
		if ((this.balance - command.getQuantity()) < 0) {
			apply(new CoinRemovedEvent(command.getCoinid(), 0, 
					command.getOrderId(),
					this.balance,command.isRevert()));
		} else {
			apply(new CoinRemovedEvent(command.getCoinid(), 
					command.getQuantity(), command.getOrderId(), this.balance,command.isRevert()));
		}
	}
	
	/**
	 * This command is used to update a coin in the system
	 * @param command UpdateCoinPriceCommand
	 */
	@CommandHandler
    private void handle(UpdateCoinPriceCommand command) {
		log.debug("Update coin command fired",command);
        if(command.getPrice().compareTo(BigDecimal.ZERO) < 0 ) {
        	return;
        }
        price = command.getPrice();
        apply(new CoinPriceUpdatedEvent(command.getCoinid(),command.getPrice()));
    }
	
	/**
	 * This is the event fired when a Coin is removed from the system.
	 * @param command
	 */
    @EventSourcingHandler
    private void on(CoinRemovedEvent event) {
    	log.debug("Remove coin event fired",event);
    	System.out.println("coin removed event entry " +this.balance + " quantity :: "+event.getQuantity());
        this.coinId = event.getCoinId();
        this.balance = this.balance - event.getQuantity();
    	System.out.println("coin removed event exit " +this.balance + " quantity :: "+event.getQuantity());
    }
    
    /**
	 * This is the event fired when a Coin is updated in the system.
	 * @param command
	 */
    @EventSourcingHandler
    private void on(CoinPriceUpdatedEvent event) {
    	log.debug("Update coin event fired",event);
        this.coinId = event.getCoinId();
    }
    
    
    
    /**
	 * This is the event fired when a Coin is registered in the system.
	 * @param command
	 */
    @EventSourcingHandler
    private void on(CoinRegisteredEvent event) {
    	log.debug("Register coin event fired",event);
    	System.out.println("coin registered event entry" +this.balance);
        this.coinId = event.getCoinId();
        this.balance = event.getBalance();
        this.price = event.getPrice();
    	System.out.println("coin registered event EXIT" +this.balance);    
    }
    
    
    /**
	 * This is the event fired when a Coin is added to the system.
	 * @param command
	 */
    @EventSourcingHandler
    private void on(CoinAddedEvent event) {
    	log.debug("Add coin event fired",event);
    	System.out.println("coin added event entry " +this.balance + " quantity :: "+event.getQuantity());
    	System.out.println(event.getCoinId());
        this.coinId = event.getCoinId();
        this.balance = this.balance + event.getQuantity();
        System.out.println("coin added event exit " +this.balance + " quantity :: "+event.getQuantity());
    }
   
    
    public CoinAggregate() {
        // Required by Axon to construct an empty instance to initiate Event Sourcing.
    }
	
}
