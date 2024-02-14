package com.quantummaintenance.users.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.quantummaintenance.users.dto.*;
import com.quantummaintenance.users.entity.*;
import com.quantummaintenance.users.repository.UsersRepository;
import com.quantummaintenance.users.service.UserService;

import io.jsonwebtoken.Claims;


@RestController
@RequestMapping("/users")
@CrossOrigin("*")
public class UsersAPI {
private RestTemplate restTemplate=new RestTemplate();
	
	String customerApi="http://localhost:8080/customer/get/";
	
	private ModelMapper modelMapper=new ModelMapper();
	
	@Autowired
    private UserService userService;
	
	 
//	 private PasswordEncoder passwordEncoder;
	
	@Autowired private UsersRepository usersRepository;

	@PostMapping("/send/{companyId}")
    public void sendEmail(@PathVariable String companyId,@RequestBody Mail mail) {
		AuthenticationResponseDTO response=userService.generateToken(mail);
		System.out.print("---------------------///////--------------------////-----------------"+response.getToken());
		userService.sendSimpleMessage(mail.getEmail(), "Invitation mail", mail.getMessage()+" http://localhost:4200/invitation/"+companyId+"/"+response.getToken());
    }
	@GetMapping(value="/getCustomer/{companyId}")
	public ResponseEntity<Customer> getCustomerDetails(@PathVariable String companyId,@RequestHeader("Authorization") String token){
	
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Customer> entity = new HttpEntity<>(headers);
		
		ResponseEntity<CustomerDTO> response = restTemplate.exchange(
				customerApi+companyId,
                HttpMethod.GET,
                entity,
                CustomerDTO.class
        );
		Customer customer=modelMapper.map(response.getBody(), Customer.class);
        return ResponseEntity.ok(customer);
	
	}
	@PostMapping(value="/invite/{companyId}/{details}")
	public void getInviteDetails(@PathVariable String companyId,@PathVariable String details,@RequestBody UsersDTO userDTO){
		Claims myDetails=userService.decodeDetails(details);
		System.out.println(userDTO);
		Users user=modelMapper.map(userDTO, Users.class);
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
		user.setCompanyId(companyId);
		if(myDetails.get("role").equals("ADMIN")) {
			user.setRole(Role.ADMIN);
		}
		else if(myDetails.get("role").equals("TECHNICAL")) {
			user.setRole(Role.TECHNICAL);
		}
		usersRepository.save(user);
	
	}
	
	@GetMapping(value="/getUsers/{companyId}")
	public ResponseEntity<List<UsersDTO>> getAllUsers(@PathVariable String companyId){
		List<UsersDTO> userList=userService.getAllUsers(companyId);
		return ResponseEntity.ok(userList);
	
	}
}
