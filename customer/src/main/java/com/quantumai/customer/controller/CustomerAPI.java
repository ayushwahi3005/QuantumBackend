package com.quantumai.customer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantumai.customer.dto.AuthenticationRequestDTO;
import com.quantumai.customer.dto.AuthenticationResponseDTO;
import com.quantumai.customer.dto.BaseResponseDTO;
import com.quantumai.customer.dto.CompanyIdDTO;
import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.entity.CompanyInformation;
import com.quantumai.customer.service.CustomerService;

@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/customer")
public class CustomerAPI {
	
	@Autowired
	private CustomerService customerService;
	

	
	@PostMapping(value="/addCustomer")
	public ResponseEntity<BaseResponseDTO> addCustomer(@RequestBody CustomerDTO customerDTO) throws Exception{
		
		
		return  ResponseEntity.ok(customerService.addCustomer(customerDTO));
	}
	@PostMapping(value="/authenticate")
	public ResponseEntity<AuthenticationResponseDTO> authenticate(@RequestBody AuthenticationRequestDTO authenticationRequestDTO) throws Exception{
		
		
		
		return ResponseEntity.ok(customerService.authenticate(authenticationRequestDTO));
	}
	@GetMapping(value="/getLoginToken/{email}")
	public ResponseEntity<AuthenticationResponseDTO> getLoginToken(@PathVariable String email) throws Exception {
		
		return ResponseEntity.ok(customerService.getLoginToken(email));
	}

	@GetMapping(value="/get/{email}")
	public CustomerDTO getCustomer(@PathVariable String email) throws Exception {
		
		return customerService.getCustomer(email);
	}
	@GetMapping(value="/getsubscription/{email}")
	public CustomerSubscribedDTO getCustomerSubscribed(@PathVariable String email ) throws Exception {
		return customerService.getCustomerSubscription(email);
	}
	@PostMapping(value="/addCompanyInformation")
	public ResponseEntity<CompanyInformation> addCompanyInformation(@RequestBody CompanyInformation companyInformation) throws Exception{
		customerService.addCompanyInformation(companyInformation);
		
		
		return ResponseEntity.ok(companyInformation);
	}
	@PostMapping(value="/updateCompanyInformation")
	public ResponseEntity<CompanyInformation> updateCompanyInformation(@RequestBody CompanyInformation companyInformation) throws Exception{
		customerService.addCompanyInformation(companyInformation);
		
		
		return ResponseEntity.ok(companyInformation);
	}
	
	@GetMapping(value="/getCompanyInformation/{email}")
	public ResponseEntity<CompanyInformation> getCompanyInformation(@PathVariable String email ) throws Exception {
		return ResponseEntity.ok(customerService.getcompanyInformation(email));
	}
	@GetMapping(value="/getCompanyId/{email}")
	public ResponseEntity<CompanyIdDTO> getCompanyId(@PathVariable String email ) throws Exception {
		return ResponseEntity.ok(customerService.getCompanyId(email));
	}
	
	
}
