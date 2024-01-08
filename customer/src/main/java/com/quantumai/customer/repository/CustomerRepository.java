package com.quantumai.customer.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.entity.Customer;

public interface CustomerRepository extends MongoRepository<Customer,String> {
	
	Boolean existsByEmail(String email);
	Optional<Customer> findByEmail(String email);

}
