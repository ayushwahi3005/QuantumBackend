package com.quantumai.customer.service;

import java.time.LocalDate;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quantumai.customer.dto.AuthenticationRequestDTO;
import com.quantumai.customer.dto.AuthenticationResponseDTO;
import com.quantumai.customer.dto.BaseResponseDTO;
import com.quantumai.customer.dto.CompanyIdDTO;
import com.quantumai.customer.dto.CustomerDTO;
import com.quantumai.customer.dto.CustomerSubscribedDTO;
import com.quantumai.customer.entity.CompanyInformation;
import com.quantumai.customer.entity.Customer;
import com.quantumai.customer.entity.CustomerSubscribed;
import com.quantumai.customer.entity.Role;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;
import com.quantumai.customer.repository.CompanyInformationRepository;
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
	
	@Autowired JwtService jwtService;
	
	@Autowired
	private AuthenticationManager authenticationManager;
	
	 
	 private ModelMapper modelMapper=new ModelMapper();
	 
	 @Autowired
	 private PasswordEncoder passwordEncoder;
	 


	@Override
	public BaseResponseDTO addCustomer(CustomerDTO customerDTO) throws Exception {
		// TODO Auto-generated method stub
		System.out.print("---------------->Called");
		if(customerDTO==null) {
			throw new Exception("Empty User");
		}
		if(customerRepository.existsByEmail(customerDTO.getEmail())) {
			throw new UserAlreadyPresentException("User Already Present");
		}
		Customer customer=modelMapper.map(customerDTO, Customer.class);
		customer.setRole(Role.ADMIN);
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
	public AuthenticationResponseDTO getLoginToken(String email) {
		// TODO Auto-generated method stub
		Optional<Customer> customer=customerRepository.findByEmail(email);
		System.out.print(customer);
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
	public CompanyInformation getcompanyInformation(String email) {
		// TODO Auto-generated method stub
		
		Optional<CompanyInformation> myCompanyInformation=companyInformationRepository.findByCustomerEmail(email);
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
		Optional<CompanyInformation> myCompanyInformation=companyInformationRepository.findByCustomerEmail(email);
		if(myCompanyInformation.isEmpty()) {
			return null;
		}
		else {
			CompanyInformation companyInformation=myCompanyInformation.get();
			CompanyIdDTO companyIdDTO=new CompanyIdDTO();
			companyIdDTO.setCompanyName(companyInformation.getCompanyName());
			companyIdDTO.setId(companyInformation.getId());
			return companyIdDTO;
		}
	}

}

