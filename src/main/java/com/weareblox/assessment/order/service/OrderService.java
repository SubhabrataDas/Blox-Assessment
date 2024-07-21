package com.weareblox.assessment.order.service;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import org.axonframework.commandhandling.gateway.CommandGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.order.api.command.CreateOrderCommand;
import com.weareblox.assessment.order.dto.Order;
import com.weareblox.assessment.order.jpa.OrderEntity;
import com.weareblox.assessment.order.jpa.OrderRepository;
import com.weareblox.assessment.order.jpa.OrderStatus;

import reactor.core.publisher.Mono;

/**
 * This is the service for processing orders.
 * This allows to place an order and get updates on the order.
 */
@Service
public class OrderService {
	
	
	@Autowired private OrderRepository repository;
	@Autowired private CommandGateway commandGateway;
	
	/**
	 * Place an order to buy or sell a coin.
	 * @param order Order
	 * @return
	 */
	public CompletableFuture<Order> create(Order order) {
		CreateOrderCommand command = new 
				CreateOrderCommand(UUID.randomUUID().toString(), 
						order.getCoinId(), order.getQuantity(),
						order.getType(), order.getCustomerId(), OrderStatus.PROCESSING.name());
		return commandGateway.send(command);
	}
	
	/**
	 * get an order details of a coin.
	 * @param order Order
	 * @return
	 */
	public Mono<Order> getOrder(String id) {
		return Mono.justOrEmpty(repository.findById(id).map(this::toDto));
	}
	
	/**
	 * get an order details of a coin.
	 * @param order Order
	 * @return
	 */
	public Mono<List<Order>> getOrders() {
		return Mono.justOrEmpty(repository.findAll().stream().map(this::toDto).toList());
	}
	
	/**
	 * get orders for an customer
	 * @param order Order
	 * @return
	 */
	public Mono<List<Order>> getCustomerOrders(String customerId) {
		return Mono.justOrEmpty(repository.
		findByCustomerId(customerId)
		.stream()
		.map(this::toDto).toList());
		
	}
	
	/**
	 * converter for getting dto from entity 
	 * @param entity
	 * @return
	 */
	Order toDto(OrderEntity entity) {
		if(entity == null) {
			return null;
		}
		return new Order(entity.getId(),
				entity.getCoinId(), 
				entity.getCustomerId(),
				entity.getQuantity(),
				entity.getType().name(), 
				entity.getStatus().name(), 
				entity.getComment());
	}

}
