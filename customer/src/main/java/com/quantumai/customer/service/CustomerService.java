package com.quantumai.customer.service;

import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;

public interface CustomerService {
	public void addCustomer(CustomerDTO customerDTO) throws UserAlreadyPresentException, Exception;
	public CustomerDTO getCustomer(String email) throws Exception;
	public CustomerSubscribedDTO getCustomerSubscription(String email) throws NoSubscriptionError ;
	public void addSubscription(String email) throws Exception;

}
