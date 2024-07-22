package com.weareblox.assessment.order.service;

import java.math.BigDecimal;
import java.util.Optional;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.axonframework.modelling.saga.SagaEventHandler;
import org.axonframework.modelling.saga.SagaLifecycle;
import org.axonframework.modelling.saga.StartSaga;
import org.axonframework.spring.stereotype.Saga;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.weareblox.assessment.coin.api.command.AddCoinCommand;
import com.weareblox.assessment.coin.api.command.RemoveCoinCommand;
import com.weareblox.assessment.coin.api.event.CoinAddedEvent;
import com.weareblox.assessment.coin.api.event.CoinRemovedEvent;
import com.weareblox.assessment.coin.jpa.CoinEntity;
import com.weareblox.assessment.coin.jpa.CoinRepository;
import com.weareblox.assessment.ledger.api.command.CreateLedgerCommand;
import com.weareblox.assessment.ledger.api.command.TransactionType;
import com.weareblox.assessment.ledger.api.event.LedgerCreateFailedEvent;
import com.weareblox.assessment.ledger.api.event.LedgerCreatedEvent;
import com.weareblox.assessment.order.api.command.CompleteOrderCommand;
import com.weareblox.assessment.order.api.command.StopOrderCommand;
import com.weareblox.assessment.order.api.event.OrderCompletedEvent;
import com.weareblox.assessment.order.api.event.OrderCreatedEvent;
import com.weareblox.assessment.order.api.event.OrderStoppedEvent;
import com.weareblox.assessment.order.jpa.OrderEntity;
import com.weareblox.assessment.order.jpa.OrderRepository;
import com.weareblox.assessment.order.jpa.OrderType;

import lombok.extern.slf4j.Slf4j;


/**
 * This is the manager for synchronising all operations following an order.
 * This uses Saga for firing a order event and handling successful or failure events for the order.
 */
@Slf4j
@Saga
@Component
public class OrderManager {

	@Autowired
	private transient CommandGateway commandGateway;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private CoinRepository coinRepository;

	/**
	 * This is the starting point for the Saga.
	 * This is triggered when an order is submitted for buying or selling of coins by a customer.
	 * This would in turn fire a event to remove or add the coins from the CoinAggregate.
	 * @param orderCreatedEvent
	 */
	@StartSaga
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCreatedEvent orderCreatedEvent) {
		
		log.debug("Starting the Order creation Saga ", orderCreatedEvent.getOrderId());
		
		String coinId = orderCreatedEvent.getCoinId();

		// associate Saga
		SagaLifecycle.associateWith("orderId", orderCreatedEvent.getOrderId());

		// send the commands 
		if (orderCreatedEvent.getType().equalsIgnoreCase(OrderType.BUY.name())) {
			//remove the coins from the system in case they are being bought by a customer
			commandGateway.send(new RemoveCoinCommand(coinId, orderCreatedEvent.getQuantity(),
					orderCreatedEvent.getOrderId(), false));
		} else {
			//add the coins to the system in case they are being sold by a customer
			commandGateway.send(
					new AddCoinCommand(coinId, orderCreatedEvent.getQuantity(), orderCreatedEvent.getOrderId(), false));
		}
	}

	/**
	 * Handles the coin remove event. This is called after coins has been successfully removed from the coin
	 * repository since a customer wants to buy coins.
	 * These coins are then unavailable to buy for other customers.
	 * This triggers a ledger add event so that the customer request to buy coin is then stored in a ledger
	 * @param coinRemovedEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(CoinRemovedEvent coinRemovedEvent) {
		log.debug("Handling the coin removed event in Saga for order ", coinRemovedEvent.getOrderId());
		//coin has not been removed successfully
		if (coinRemovedEvent.isRevert() || coinRemovedEvent.getOrderId() == null) {
			return;
		}
		SagaLifecycle.associateWith("orderId", coinRemovedEvent.getOrderId());
		// revert coin removed from repository since the removed quantity is zero.
		if (coinRemovedEvent.getQuantity() == 0) {
			commandGateway.send(new StopOrderCommand(coinRemovedEvent.getOrderId()));
			return;
		}
		Optional<OrderEntity> orderEntity = orderRepository.findById(coinRemovedEvent.getOrderId());
		Optional<CoinEntity> coinEntity = coinRepository.findById(coinRemovedEvent.getCoinId());
		if (orderEntity.isPresent() && coinEntity.isPresent()) {
			OrderEntity order = orderEntity.get();
			CoinEntity coin = coinEntity.get();
			log.debug("Sending event for ledger creation in Saga ", coinRemovedEvent.getOrderId());
			// Initiate a ledger create event to store the result of the transaction
			CreateLedgerCommand command = new CreateLedgerCommand(order.getCustomerId(), order.getCoinId(),
					coin.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())), order.getQuantity(),
					TransactionType.valueOf(order.getType().name()), coin.getCommision(), order.getId());
			commandGateway.send(command);
		}

	}
	
	
	/**
	 * Handles the coin add event. This is called after coins has been successfully added to the coin
	 * repository since a customer wants to sell coins.
	 * The coins are then available to sell for other customers.
	 * This triggers a ledger add event so that the customer request to sell coin is then stored in a ledger
	 * @param CoinAddedEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(CoinAddedEvent coinAddedEvent) {
		log.debug("adding event for coin addition in Saga ", coinAddedEvent.getOrderId());
		if (coinAddedEvent.getOrderId() != null && !coinAddedEvent.isRevert()) {
			Optional<OrderEntity> orderEntity = orderRepository.findById(coinAddedEvent.getOrderId());
			Optional<CoinEntity> coinEntity = coinRepository.findById(coinAddedEvent.getCoinId());

			if (orderEntity.isPresent() && coinEntity.isPresent()) {
				OrderEntity order = orderEntity.get();
				CoinEntity coin = coinEntity.get();
				SagaLifecycle.associateWith("orderId", order.getId());
				// add a create ledger event to store the record of coin sell
				log.debug("Firing a ledger create oder in Saga ", coinAddedEvent.getOrderId());
				CreateLedgerCommand command = new CreateLedgerCommand(order.getCustomerId(), order.getCoinId(),
						coin.getPrice().multiply(BigDecimal.valueOf(order.getQuantity())), order.getQuantity(),
						TransactionType.valueOf(order.getType().name()), coin.getCommision(), order.getId());
				commandGateway.send(command);
			}
		}

	}

	/**
	 * Handles the ledger create event. This is triggered from the Ledger creation process after a 
	 * customer order of selling or buying coins.
	 * This would then call the Complete Order command to successfully complete the order.
	 * @param orderEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(LedgerCreatedEvent orderEvent) {
		log.debug("Handling successful ledger creation event for an order ", orderEvent.getOrderId());
		SagaLifecycle.associateWith("orderId", orderEvent.getOrderId());
		commandGateway.send(new CompleteOrderCommand(orderEvent.getOrderId()));
	}

	/**
	 * Handles the ledger failure event. This is triggered from the Ledger creation process after a 
	 * customer order of selling or buying coins. The failure can happen in case customer is trying to sell
	 * more coins than they have.
	 * @param orderEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(LedgerCreateFailedEvent orderEvent) {
		log.debug("Handling ledger failed event for an order in Saga ", orderEvent.getOrderId());
		SagaLifecycle.associateWith("orderId", orderEvent.getOrderId());
		Optional<OrderEntity> orderEntity = orderRepository.findById(orderEvent.getOrderId());
		if (orderEntity.isPresent()) {
			OrderEntity entity = orderEntity.get();
			// revert initial coin add/remove actions since the process has failed
			if (entity.getType().name().equalsIgnoreCase(OrderType.BUY.name())) {
				commandGateway
						.send(new AddCoinCommand(orderEvent.getCoinId(), entity.getQuantity(),null, true));
			} else {
				commandGateway.send(
						new RemoveCoinCommand(orderEvent.getCoinId(), entity.getQuantity(), null, true));
			}

		}
		// stop the order as there has been a problem with processing the order
		commandGateway.send(new StopOrderCommand(orderEvent.getOrderId()));
	}

	/**
	 * This handles the Order stopped event fired since the Order creation process failed.
	 * This updates the order status and ends the Saga lifecycle
	 * @param orderEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderStoppedEvent orderEvent) {
		log.debug("Handling Order stopped event for an order in Saga ", orderEvent.getOrderId());
		SagaLifecycle.end();
	}

	/**
	 * This handles the Order completed event fired since the Order is successfully processed.
	 * This updates the order status and ends the Saga life cycle
	 * @param orderEvent
	 */
	@SagaEventHandler(associationProperty = "orderId")
	public void handle(OrderCompletedEvent orderEvent) {
		log.debug("Handling Oder completed event for an order in Saga ", orderEvent.getOrderId());
		SagaLifecycle.end();
	}

}
