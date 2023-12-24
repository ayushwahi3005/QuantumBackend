package com.quantumai.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.service.CustomerService;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/customer")
public class CustomerAPI {
	
	@Autowired
	private CustomerService customerService;
	
	@PostMapping(value="/addCustomer")
	public void addCustomer(@RequestBody CustomerDTO customerDTO) throws Exception{
		System.out.print("-------------------------------->"+customerDTO.getMobileNumber());
		customerService.addCustomer(customerDTO);
	}
	@GetMapping(value="/get/{email}")
	public CustomerDTO getCustomer(@PathVariable String email) throws Exception {
		
		return customerService.getCustomer(email);
	}
	@GetMapping(value="/getsubscription/{email}")
	public CustomerSubscribedDTO getCustomerSubscribed(@PathVariable String email ) throws Exception {
		return customerService.getCustomerSubscription(email);
	}
	
	
}
