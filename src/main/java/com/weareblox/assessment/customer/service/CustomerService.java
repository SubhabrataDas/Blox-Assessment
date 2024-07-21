package com.weareblox.assessment.customer.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.weareblox.assessment.customer.dto.Customer;
import com.weareblox.assessment.customer.jpa.CustomerEntity;
import com.weareblox.assessment.customer.jpa.CustomerRepository;

/**
 * Service for handling customer creating and accessing methods.
 */
@Service
public class CustomerService {

	@Autowired private CustomerRepository customerRepository;

	/**
	 * Creates a customer. Throws illegal argument in case id or name is empty
	 * @param customer Customer
	 * @return
	 */
	public Customer createCustomer(Customer customer) {
		CustomerEntity customerEntity = new CustomerEntity();
		customerEntity.setName(customer.getName());
		customerEntity.setId(StringUtils.isNotBlank(customer.getId())?customer.getId():UUID.randomUUID().toString());
		customerRepository.save(customerEntity);
		return toDto(customerEntity);
	}

	/**
	 * gets all customer in the system
	 * @return
	 */
	public List<Customer> getCustomers() {
		Optional<List<CustomerEntity>> customers = Optional.of(customerRepository.findAll());
		if (customers.isPresent()) {
			return customerRepository.findAll().stream().filter(Objects::nonNull).map(entity -> toDto(entity)).toList();
		} else {
			return Collections.emptyList();
		}
	}

	/**
	 * utility method to convert entity to dto
	 * @param customerEntity
	 * @return
	 */
	private Customer toDto(CustomerEntity customerEntity) {
		if (customerEntity == null) {
			return null;
		}
		return new Customer(customerEntity.getId(), customerEntity.getName());

	}

}
