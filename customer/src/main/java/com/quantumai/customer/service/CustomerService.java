package com.quantumai.customer.service;

import java.util.List;
import java.util.Optional;

import com.quantumai.customer.dto.*;

import com.quantumai.customer.entity.AccountLockInfo;
import com.quantumai.customer.entity.CompanyInformation;
import com.quantumai.customer.entity.CustomRole;
import com.quantumai.customer.exception.CustomerException;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;
import com.quantumai.customer.exception.UserNotFound;

public interface CustomerService {
	public BaseResponseDTO addCustomer(CustomerDTO customerDTO) throws UserAlreadyPresentException, Exception;
	public CustomerDTO getCustomer(String email) throws Exception;
	public CustomerSubscribedDTO getCustomerSubscription(String email) throws NoSubscriptionError ;
	public void addSubscription(String email) throws Exception;
	public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws Exception;
	public AuthenticationResponseDTO getLoginToken(String email) throws UserNotFound;
	public void addCompanyInformation(CompanyInformation companyInformation);
	public CompanyInformation getcompanyInformation(String companyId);
	public CompanyIdDTO getCompanyId(String email);
	public BaseResponseDTO addUsers(CustomerDTO customerDTO);
	public List<String> activeUsers(String companyId);
	public AccountLockInfoDTO getAccountInfo(String email);
	public void updateAccountInfo(AccountLockInfoDTO accountLockInfo);
	public void deleteUser(String companyId,String email) throws CustomerException;
	public void addRoleAndPermission(CustomRoleDTO customRoleDTO);
	public void deleteRoleAndPermission(String customRoleId) throws Exception;
	public List<CustomRoleDTO> getRoleAndPermission(String companyId);
	public Long countByRoleName(String name,String companyId);
	public CustomRoleDTO roleAndPermissionByName(String companyId, String name);

}
