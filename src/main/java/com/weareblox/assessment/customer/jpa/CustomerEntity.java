package com.weareblox.assessment.customer.jpa;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Data;

@Entity
@Table(name = "customer")
@Data
public class CustomerEntity {

	@Id
	private String id;

	private String name;

}
