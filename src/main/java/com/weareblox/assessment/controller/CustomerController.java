package com.weareblox.assessment.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.weareblox.assessment.customer.dto.Customer;
import com.weareblox.assessment.customer.service.CustomerService;
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
 * Controller for creating and getting customers
 */
@Slf4j
@RestController
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
@RequestMapping(
        path = "/customer")
public class CustomerController {
	
	@Autowired private CustomerService customerService;
	@Autowired private OrderService orderService;
	
	/**
	 * Create a customer.
	 * @param customer Customer to create
	 * @return
	 */
	@Operation(summary = "Create a customer.")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Returns the created customer")
			  })
	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Customer addCustomer(@Valid @RequestBody Customer customer) {
		return customerService.createCustomer(customer);
	}
	
	@Operation(summary = "Get all customers in the system")
	@ApiResponses(value = { 
			  @ApiResponse(responseCode = "200", description = "Returns the customers")
			  })
	@GetMapping
	public List<Customer> getCustomers() {
		return customerService.getCustomers();
	}
	

	@Operation(summary = "Gets an order with id")
	@ApiResponses(value = { @ApiResponse(responseCode = "200", description = "Returns Order for customers") })
	@GetMapping("/{id}/orders")
	public Mono<List<Order>> getCustomerOrders(@Parameter(description = "Id of the customer") 
												@PathVariable("id") String customerId) {
		return orderService.getCustomerOrders(customerId);
	}

}
