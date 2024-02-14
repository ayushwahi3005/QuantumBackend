package com.quantumai.customer.service;

import com.quantumai.customer.dto.AuthenticationRequestDTO;
import com.quantumai.customer.dto.AuthenticationResponseDTO;
import com.quantumai.customer.dto.BaseResponseDTO;
import com.quantumai.customer.dto.CompanyIdDTO;
import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.entity.CompanyInformation;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;

public interface CustomerService {
	public BaseResponseDTO addCustomer(CustomerDTO customerDTO) throws UserAlreadyPresentException, Exception;
	public CustomerDTO getCustomer(String email) throws Exception;
	public CustomerSubscribedDTO getCustomerSubscription(String email) throws NoSubscriptionError ;
	public void addSubscription(String email) throws Exception;
	public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws Exception;
	public AuthenticationResponseDTO getLoginToken(String email);
	public void addCompanyInformation(CompanyInformation companyInformation);
	public CompanyInformation getcompanyInformation(String email);
	public CompanyIdDTO getCompanyId(String email);

}
