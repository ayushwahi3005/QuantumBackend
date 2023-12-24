package com.quantumai.customer.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.entity.Customer;
import com.quantumai.customer.entity.CustomerSubscribed;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;
import com.quantumai.customer.repository.CustomerRepository;
import com.quantumai.customer.repository.CustomerSubscribedRepository;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository; 
	@Autowired
	private CustomerSubscribedRepository customerSubscribedRepository;
	
	 
	 private ModelMapper modelMapper=new ModelMapper();

	@Override
	public void addCustomer(CustomerDTO customerDTO) throws Exception {
		// TODO Auto-generated method stub
		if(customerDTO==null) {
			throw new Exception("Empty User");
		}
		if(customerRepository.existsByEmail(customerDTO.getEmail())) {
			throw new UserAlreadyPresentException("User Already Present");
		}
		Customer customer=modelMapper.map(customerDTO, Customer.class);
		customerRepository.save(customer);
		
	}

	@Override
	public CustomerDTO getCustomer(String email) throws Exception {
		// TODO Auto-generated method stub
		if(email.isEmpty()) {
			throw new Exception("Empty email");
		}
		return customerRepository.findByEmail(email);
		
	}

	@Override
	public CustomerSubscribedDTO getCustomerSubscription(String email) throws NoSubscriptionError {
		// TODO Auto-generated method stub
		if(email.isEmpty()) {
			throw new NoSubscriptionError("Empty email");
		}
		Optional<CustomerSubscribed> customerSubscribed=customerSubscribedRepository.findById(email);
		
		if(customerSubscribed.isEmpty() || customerSubscribed.get().getLastDate().isBefore(LocalDate.now())) {
			throw new NoSubscriptionError("No subscription");
		}
		
		CustomerSubscribedDTO customerSubscribedDTO=modelMapper.map(customerSubscribed.get(), CustomerSubscribedDTO.class);
		return customerSubscribedDTO;
	}

	@Override
	public void addSubscription(String email) throws Exception {
		// TODO Auto-generated method stub
		if(email.isEmpty()) {
			throw new Exception("Empty email");
		}
		Optional<CustomerSubscribed> optionalCustomerSubscribed=customerSubscribedRepository.findById(email);
		CustomerSubscribed customerSubscribed=new CustomerSubscribed();
		
		if(optionalCustomerSubscribed.isEmpty()) {
			customerSubscribed.setEmail(email);
			customerSubscribed.setLastDate(LocalDate.now().plusMonths(1));
			customerSubscribed.setSubscription(true);
		}
		else {
			customerSubscribed=optionalCustomerSubscribed.get();
			customerSubscribed.setLastDate(LocalDate.now().plusMonths(1));
			customerSubscribed.setSubscription(true);
		}
		
		customerSubscribedRepository.save(customerSubscribed);
	}

}

