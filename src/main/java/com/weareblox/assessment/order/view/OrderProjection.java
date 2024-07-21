package com.weareblox.assessment.order.view;

import java.util.Optional;

import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.axonframework.queryhandling.QueryHandler;
import org.axonframework.queryhandling.QueryUpdateEmitter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.order.api.event.OrderCompletedEvent;
import com.weareblox.assessment.order.api.event.OrderCreatedEvent;
import com.weareblox.assessment.order.api.event.OrderStoppedEvent;
import com.weareblox.assessment.order.api.query.FindOrderById;
import com.weareblox.assessment.order.dto.Order;
import com.weareblox.assessment.order.jpa.OrderEntity;
import com.weareblox.assessment.order.jpa.OrderRepository;
import com.weareblox.assessment.order.jpa.OrderStatus;
import com.weareblox.assessment.order.jpa.OrderType;

import lombok.RequiredArgsConstructor;


/**
 * Projection class for Order to handle events and then update the database with a  
 * view of the Order.
 */
@Service
@ProcessingGroup("order")
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class OrderProjection {
	
    @Autowired
	private final OrderRepository repository;
    @Autowired
    private final QueryUpdateEmitter updateEmitter;
    
    /**
     * this handles successful order creation event . This would finally save the entity in the database.
     * @param event
     */
    @EventHandler
    public void on(OrderCreatedEvent event) {
    	OrderEntity orderEntity = toEntity(event);
    	if(orderEntity == null) {
    		return;
    	}
    	repository.save(orderEntity);
    }
    
    /**
     * This handles the order completion event. This is fired on successful order processing.
     * @param event
     */
    @EventHandler
    public void on(OrderCompletedEvent event) {
    	repository.findById(event.getOrderId())
    	.ifPresent(order -> {
    		order.setStatus(OrderStatus.COMPLETED);
    		repository.save(order);
    	});
    	
    }
    
    /**
     * This handles the order stopped event. This is fired on order processing failure.
     * @param event
     */
    @EventHandler
    public void on(OrderStoppedEvent event) {
    	repository.findById(event.getOrderId())
    	.ifPresent(order -> {
    		order.setStatus(OrderStatus.STOPPED);
    		//order.setComment(event.getComment());
    		repository.save(order);
    	});
    	
    }
    
    
    /**
     * this is query handler for finding a order by id.
     * @param query
     * @return
     */
    @QueryHandler
    public Optional<Order> findOrder(FindOrderById query) {
        return repository.findById(query.getOrderId())
                .map(this::toDto);
    }
    
    
    /**
     * this is utility method for converting an entity to DTO.
     * @param entity
     * @return
     */
	private Order toDto(OrderEntity entity) {
		Order order = new Order(entity.getId(),
				entity.getCoinId(),entity.getCustomerId(),
				entity.getQuantity(),entity.getType().name(), 
				entity.getStatus().name(),entity.getComment());
		
		return order;
	}
    
	/**
	 * this is utility method for converting an order created event to OrderEntity
	 * @param event
	 * @return
	 */
    private OrderEntity toEntity(OrderCreatedEvent event) {
		if(event == null) {
            return null;
        }
		OrderEntity entity = new OrderEntity();
		entity.setCoinId(event.getCoinId());
		entity.setId(event.getOrderId());
		entity.setCustomerId(event.getCustomerid());
		entity.setQuantity(event.getQuantity());
		entity.setStatus(OrderStatus.PROCESSING);
		entity.setType(OrderType.valueOf(event.getType()));
		return entity;
    }
}
