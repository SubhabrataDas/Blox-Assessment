package com.weareblox.assessment.order.command;

import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import com.weareblox.assessment.order.api.command.CompleteOrderCommand;
import com.weareblox.assessment.order.api.command.CreateOrderCommand;
import com.weareblox.assessment.order.api.command.StopOrderCommand;
import com.weareblox.assessment.order.api.event.OrderCompletedEvent;
import com.weareblox.assessment.order.api.event.OrderCreatedEvent;
import com.weareblox.assessment.order.api.event.OrderStoppedEvent;
import com.weareblox.assessment.order.jpa.OrderStatus;

import lombok.extern.slf4j.Slf4j;

/**
 * This is the aggregate for the Order.
 * This handles the commands for creating an order, submitting a successful order or stopping the order
 * in case of an error.
 */

@Slf4j
@Aggregate
public class OrderAggregate {
	
	@AggregateIdentifier
    private String id;
	
	private String coinId;
	
	private int quantity;
	
	private String type;
	
	private String status;
	
	private String customerid;
	
	/**
	 * This is the command for creating an order.
	 * @param command
	 */
	@CommandHandler
    public OrderAggregate(CreateOrderCommand command) {
	        id = command.getOrderId();
	        status = OrderStatus.PROCESSING.name();
	        customerid = command.getCustomerid();
	        quantity = command.getQuantity();
	        coinId = command.getCoinId();
	        apply(OrderCreatedEvent
	        		.builder()
	        		.orderId(command.getOrderId())
	        		.coinId(command.getCoinId())
	        		.customerid(command.getCustomerid())
	        		.quantity(command.getQuantity())
	        		.type(command.getType()).build());
    }
	
	/**
	 * This is the Command handler for a completed order.
	 * This is fired when a order is completed successfully.
	 * @param command
	 */
	@CommandHandler
    private void handle(CompleteOrderCommand command) {
		id = command.getOrderId();
        status = OrderStatus.COMPLETED.name();
        apply(new OrderCompletedEvent(command.getOrderId()));
    }
	
	/**
	 * This is the handler for a stop order command. This is fired for a unsuccessful order.
	 * @param command
	 */
	@CommandHandler
	private void handle(StopOrderCommand command) {
		id = command.getOrderId();
		status = OrderStatus.STOPPED.name();
		apply(new OrderStoppedEvent(command.getOrderId()));
	}
	
	/**
	 * this is the event for order creation.
	 * @param event
	 */
	@EventSourcingHandler
	private void on(OrderCreatedEvent event) {
		this.id = event.getOrderId();
	}

	/**
	 * this is the event for a successful order completion.
	 * @param event
	 */
	@EventSourcingHandler
	private void on(OrderCompletedEvent event) {
		this.id = event.getOrderId();
	}
	
	/**
	 * This is the event for a unsuccessful order.
	 * @param event
	 */
	@EventSourcingHandler
	private void on(OrderStoppedEvent event) {
		this.id = event.getOrderId();
	}
	
	public OrderAggregate() {
	}
	

}
