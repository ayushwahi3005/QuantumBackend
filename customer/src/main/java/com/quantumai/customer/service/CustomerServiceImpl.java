package com.quantumai.customer.service;

import java.time.LocalDate;
import java.util.List;
import java.util.ArrayList;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quantumai.customer.dto.AccountLockInfoDTO;
import com.quantumai.customer.dto.AuthenticationRequestDTO;
import com.quantumai.customer.dto.AuthenticationResponseDTO;
import com.quantumai.customer.dto.BaseResponseDTO;
import com.quantumai.customer.dto.CompanyIdDTO;
import com.quantumai.customer.dto.CustomRoleDTO;
import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.entity.AccountLockInfo;
import com.quantumai.customer.entity.CompanyInformation;
import com.quantumai.customer.entity.CustomRole;
import com.quantumai.customer.entity.Customer;
import com.quantumai.customer.entity.CustomerSubscribed;
import com.quantumai.customer.entity.Role;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;
import com.quantumai.customer.exception.UserNotFound;
import com.quantumai.customer.exception.CustomerException;
import com.quantumai.customer.repository.AccountLockInfoRepository;
import com.quantumai.customer.repository.CompanyInformationRepository;
import com.quantumai.customer.repository.CustomRoleRepository;
import com.quantumai.customer.repository.CustomerRepository;
import com.quantumai.customer.repository.CustomerSubscribedRepository;
import com.quantumai.customer.security.JwtService;

@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
	private CustomerRepository customerRepository; 
	@Autowired
	private CustomerSubscribedRepository customerSubscribedRepository;
	
	@Autowired
	private CompanyInformationRepository companyInformationRepository;
	
	@Autowired private AccountLockInfoRepository accountLockInfoRepository;
	
	@Autowired JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	 
	 private ModelMapper modelMapper=new ModelMapper();
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 @Autowired
		private CustomRoleRepository customRoleRepository;
	 


	@Override
	public BaseResponseDTO addCustomer(CustomerDTO customerDTO) throws Exception {
		// TODO Auto-generated method stub
//		System.out.print("---------------->Called");
		if(customerDTO==null) {
			throw new Exception("Empty User");
		}
		if(customerRepository.existsByEmail(customerDTO.getEmail())) {
			throw new UserAlreadyPresentException("User Already Present");
		}
		Customer customer=modelMapper.map(customerDTO, Customer.class);
		customer.setRole("ADMIN");
		customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
		customerRepository.save(customer);
		BaseResponseDTO baseResponseDTO=new BaseResponseDTO();
		baseResponseDTO.setSucess(true);
		baseResponseDTO.setMessage("User Successfully Created");
		
		
		return baseResponseDTO;
		
	}

	@Override
	public CustomerDTO getCustomer(String email) throws Exception {
		// TODO Auto-generated method stub
		if(email.isEmpty()) {
			throw new Exception("Empty email");
		}
		Optional<Customer> customer=customerRepository.findByEmail(email);
		CustomerDTO customerDTO=modelMapper.map(customer.get(), CustomerDTO.class);
		return customerDTO;
		
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

	@Override
	public AuthenticationResponseDTO authenticate(AuthenticationRequestDTO authenticationRequestDTO) throws Exception {
		// TODO Auto-generated method stub
		authenticationManager.authenticate(
				new UsernamePasswordAuthenticationToken(authenticationRequestDTO.getEmail(),authenticationRequestDTO.getPassword())
				);
		var user=customerRepository.findByEmail(authenticationRequestDTO.getEmail()).orElseThrow(()->new Exception("Not Present"));
		var jwtToken=jwtService.generateToken(user);
		return AuthenticationResponseDTO.builder().token(jwtToken).build();
	}

	@Override
	public AuthenticationResponseDTO getLoginToken(String email) throws UserNotFound {
		// TODO Auto-generated method stub
		Optional<Customer> customer=customerRepository.findByEmail(email);
		System.out.println("////"+customer);
		if(customer.isEmpty()) {
			throw new UserNotFound("User Not Associated to any company");
		}
//		System.out.print(customer);
		var jwtToken=jwtService.generateToken(customer.get());
		
		
		return AuthenticationResponseDTO.builder().token(jwtToken).role(customer.get().getRole()).build();
		
	}

	@Override
	public void addCompanyInformation(CompanyInformation companyInformation) {
		// TODO Auto-generated method stub
		String email=companyInformation.getCustomerEmail();
		Optional<CompanyInformation> myCompanyInformation=companyInformationRepository.findByCustomerEmail(email);
		if(myCompanyInformation.isEmpty()) {
			companyInformationRepository.save(companyInformation);
		}
		else {
			companyInformation.setId(myCompanyInformation.get().getId());
			companyInformationRepository.save(companyInformation);
		}
		
		
	}

	@Override
	public CompanyInformation getcompanyInformation(String companyId) {
		// TODO Auto-generated method stub
		
		Optional<CompanyInformation> myCompanyInformation=companyInformationRepository.findById(companyId);
		if(myCompanyInformation.isEmpty()) {
			return null;
		}
		else {
			
			return myCompanyInformation.get();
		}
	}

	@Override
	public CompanyIdDTO getCompanyId(String email) {
		// TODO Auto-generated method stub
		Optional<Customer> myCompanyInformation=customerRepository.findByEmail(email);
		if(myCompanyInformation.isEmpty()) {
			return null;
		}
		else {
			Customer companyInformation=myCompanyInformation.get();
			CompanyIdDTO companyIdDTO=new CompanyIdDTO();
			companyIdDTO.setCompanyName(companyInformation.getCompanyName());
			companyIdDTO.setId(companyInformation.getCompanyId());
			return companyIdDTO;
		}
	}

	@Override
	public BaseResponseDTO addUsers(CustomerDTO customerDTO) {
		// TODO Auto-generated method stub
		Customer customer=modelMapper.map(customerDTO, Customer.class);

		customer.setPassword(passwordEncoder.encode(customerDTO.getPassword()));
		customerRepository.save(customer);
		BaseResponseDTO baseResponseDTO=new BaseResponseDTO();
		baseResponseDTO.setSucess(true);
		baseResponseDTO.setMessage("User Successfully Created");
		
		
		return baseResponseDTO;
	}

	@Override
	public List<String> activeUsers(String companyId) {
		// TODO Auto-generated method stub
		List<Customer> customerList=customerRepository.findByCompanyId(companyId);
		List<String> myCustomerList=new ArrayList<>();
		customerList.stream().forEach((x)->{
			myCustomerList.add(x.getEmail());
		});
		return myCustomerList;
	}

	@Override
	public AccountLockInfoDTO getAccountInfo(String email) {
		// TODO Auto-generated method stub
		AccountLockInfoDTO accountLockInfoDTO=null;
		Optional<AccountLockInfo> accountLockInfoOptional=accountLockInfoRepository.findByCustomerEmail(email);
		if(accountLockInfoOptional.isPresent()) {
			AccountLockInfo accountLockInfo= accountLockInfoOptional.get();
			accountLockInfoDTO= modelMapper.map(accountLockInfo, AccountLockInfoDTO.class);
			
		}
		return accountLockInfoDTO;
	}

	@Override
	public void updateAccountInfo(AccountLockInfoDTO accountLockInfoDTO) {
		// TODO Auto-generated method stub
		AccountLockInfo accountLockInfo=modelMapper.map(accountLockInfoDTO, AccountLockInfo.class);
		accountLockInfoRepository.save(accountLockInfo);
	}

	@Override
	public void deleteUser(String companyId, String email) throws CustomerException {
		// TODO Auto-generated method stub
		
		Optional<Customer> customerOptional=customerRepository.findByEmailAndCompanyId(email,companyId);
		if(customerOptional.isPresent()) {
			System.out.println("Deleting cutomer :"+email);
			customerRepository.delete(customerOptional.get());
		}
		else {
			throw new CustomerException("Customer Not Present");
		}
		
		
	}

	@Override
	public void addRoleAndPermission(CustomRoleDTO customRoleDTO) {
		// TODO Auto-generated method stub
//		customRoleRepository
		CustomRole customRole = modelMapper.map(customRoleDTO, CustomRole.class);
		System.out.print("-------> permission"+customRole.getAssets());
		customRoleRepository.save(customRole);
		
	}

	@Override
	public void deleteRoleAndPermission(String customRoleId) throws Exception {
		// TODO Auto-generated method stub
		Optional<CustomRole> customRoleOptinal=customRoleRepository.findById(customRoleId);
		if(customRoleOptinal.isPresent()) {
			customRoleRepository.delete(customRoleOptinal.get());
		}
		else {
			throw new Exception("Error in deleteing role");
		}
		
	}

	@Override
	public List<CustomRoleDTO> getRoleAndPermission(String companyId) {
		// TODO Auto-generated method stub
		List<CustomRole> customRoleList=customRoleRepository.findByCompanyId(companyId);
		List<CustomRoleDTO> customRoleDTOList= new ArrayList<>();
		customRoleList.stream().forEach((x)->{
			CustomRoleDTO customRoleDTO = modelMapper.map(x, CustomRoleDTO.class);
			customRoleDTOList.add(customRoleDTO);
			
		});
		return customRoleDTOList;
	}

	@Override
	public Long countByRoleName(String name,String companyId) {
		// TODO Auto-generated method stub
		Long count=customerRepository.countByRoleAndCompanyId(name,companyId);
		return count;
	}

	@Override
	public CustomRoleDTO roleAndPermissionByName(String companyId,String name) {
		// TODO Auto-generated method stub
		Optional<CustomRole> customRoleOptional=customRoleRepository.findByNameAndCompanyId(name, companyId);
		if(customRoleOptional.isEmpty()) {
			return null;
		}
		
			CustomRoleDTO customRoleDTO = modelMapper.map(customRoleOptional.get(), CustomRoleDTO.class);
		
			
		
		return customRoleDTO;
	}

}

