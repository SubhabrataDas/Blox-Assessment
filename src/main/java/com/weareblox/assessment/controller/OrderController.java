package com.weareblox.assessment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.weareblox.assessment.order.dto.Order;
import com.weareblox.assessment.order.service.OrderService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

/**
 * Controller for handling orders in the system for buy and sell.
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(path = "/order")
public class OrderController {
	
	@Autowired
	private OrderService orderService;
	
	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Submit an order to either buy or sell coins for a customer")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Returns the order. "
			  		+ "Async operation since orders would be validated before committing")
			  })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Mono<Order> createOrder(@Valid @RequestBody Order order) {
		return Mono.fromFuture(orderService.create(order));
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Gets an order with id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns an Order") })
	@GetMapping("/{id}")
	public Mono<Order> getOrder(@Parameter(description = "Id of the coin") @PathVariable("id") String id) {
		return orderService.getOrder(id);
	}
	
	@CrossOrigin(origins = "http://localhost:4200")
	@Operation(summary = "Gets all orders")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns all Orders") })
	@GetMapping()
	public Mono<List<Order>> getOrders() {
		return orderService.getOrders();
	}

}
