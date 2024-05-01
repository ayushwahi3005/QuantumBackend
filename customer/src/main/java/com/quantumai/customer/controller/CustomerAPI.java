package com.quantumai.customer.controller;

import java.lang.System.Logger;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantumai.customer.dto.AccountLockInfoDTO;
import com.quantumai.customer.dto.AuthenticationRequestDTO;
import com.quantumai.customer.dto.AuthenticationResponseDTO;
import com.quantumai.customer.dto.BaseResponseDTO;
import com.quantumai.customer.dto.CompanyIdDTO;
import com.quantumai.customer.dto.CustomRoleDTO;
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
	
	@GetMapping(value="/working")
	public ResponseEntity<String> working() throws Exception {
		
		return ResponseEntity.ok("Working OK");
	}
	
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
	
	@GetMapping(value="/getCompanyInformation/{companyId}")
	public ResponseEntity<CompanyInformation> getCompanyInformation(@PathVariable String companyId ) throws Exception {
		return ResponseEntity.ok(customerService.getcompanyInformation(companyId));
	}
	@GetMapping(value="/getCompanyId/{email}")
	public ResponseEntity<CompanyIdDTO> getCompanyId(@PathVariable String email ) throws Exception {
		return ResponseEntity.ok(customerService.getCompanyId(email));
	}
	@PostMapping(value="/addUser")
	public ResponseEntity<BaseResponseDTO> addUser(@RequestBody CustomerDTO customerDTO) throws Exception{
		
		
		return  ResponseEntity.ok(customerService.addUsers(customerDTO));
	}
	@GetMapping(value="/getRegisteredUsers/{companyId}")
	public ResponseEntity<List<String>> getRegisteredUsers(@PathVariable String companyId ) throws Exception {
		return ResponseEntity.ok(customerService.activeUsers(companyId));
	}
	
	@GetMapping(value="/accountInfo/{customerEmail}")
	public ResponseEntity<AccountLockInfoDTO> getAccountInfo(@PathVariable String customerEmail ) throws Exception {
		return ResponseEntity.ok(customerService.getAccountInfo(customerEmail));
	}
	@PostMapping(value="/accountInfo/update")
	public void addUser(@RequestBody AccountLockInfoDTO AccountLockInfoDTO) throws Exception{
		
		
		customerService.updateAccountInfo(AccountLockInfoDTO);
	}
	@DeleteMapping(value="/deleteAccount/{companyId}/{email}")
	public void deleteUser(@PathVariable String companyId,@PathVariable String email) throws Exception{
		
		
		customerService.deleteUser(companyId, email);
	}
	@PostMapping(value="/roleAndPermission/add")
	public void addRoleAndPermission(@RequestBody CustomRoleDTO customRoleDTO) {
		
		
		customerService.addRoleAndPermission(customRoleDTO);
		
	}
	@PutMapping(value="/roleAndPermission/update")
	public void updateRoleAndPermission(@RequestBody CustomRoleDTO customRoleDTO) {
		
		
		customerService.addRoleAndPermission(customRoleDTO);
		
	}
	@GetMapping(value="/roleAndPermission/get/{companyId}")
	public ResponseEntity<List<CustomRoleDTO>> getRoleAndPermission(@PathVariable String  companyId) throws Exception {
		
		
		return ResponseEntity.ok(customerService.getRoleAndPermission(companyId)) ;
		
	}
	@DeleteMapping(value="/roleAndPermission/delete/{id}")
	public void deleteRoleAndPermission(@PathVariable String  id) throws Exception {
		
		
		customerService.deleteRoleAndPermission(id);
		
	}
	@GetMapping(value="/countByRole/{companyId}/{roleName}")
	public ResponseEntity<Long> countByRole(@PathVariable String  companyId,@PathVariable String  roleName) throws Exception {
		Long count=customerService.countByRoleName(roleName,companyId);
		System.out.println("count->"+count);
		return ResponseEntity.ok(count) ;
		
	}
	@GetMapping(value="/roleAndPermissionByName/get/{companyId}/{name}")
	public ResponseEntity<CustomRoleDTO> roleAndPermissionByName(@PathVariable String  companyId,@PathVariable String  name) throws Exception {
		
		
		return ResponseEntity.ok(customerService.roleAndPermissionByName(companyId,name)) ;
		
	}
	
	
}
