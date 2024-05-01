package com.quantummaintenance.users.controller;

import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
import com.quantummaintenance.users.exception.UserException;
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
	
//	@Autowired
//	 private PasswordEncoder passwordEncoder;
	
	@Autowired private UsersRepository usersRepository;
	
	@PostMapping("/registerUser")
	public void register(@RequestBody Users users) throws UserException{
		
	
		
		userService.registerUser(users);
		
	}
	

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
	@PostMapping(value="/invite")
	public void getInviteDetails(@RequestBody UsersDTO userDTO) throws UserException{
//		Claims myDetails=userService.decodeDetails(details);
		System.out.println(userDTO);
		Users user=modelMapper.map(userDTO, Users.class);
//		user.setPassword(passwordEncoder.encode(user.getPassword()));
//		user.setCompanyId(companyId);
//		if(myDetails.get("role").equals("ADMIN")) {
//			user.setRole(Role.ADMIN);
//		}s
//		else if(myDetails.get("role").equals("TECHNICAL")) {
//			user.setRole(Role.TECHNICAL);
//		}
		userService.registerUser(user);
	
	}
	
	@GetMapping(value="/getUsers/{companyId}")
	public ResponseEntity<List<UsersDTO>> getAllUsers(@PathVariable String companyId){
		List<UsersDTO> userList=userService.getAllUsers(companyId);
		return ResponseEntity.ok(userList);
	
	}
	@GetMapping(value="/invite/getUser/{companyId}/{details}")
	public ResponseEntity<UsersDTO> getUsers(@PathVariable String companyId,@PathVariable String details){
		Claims myDetails=userService.decodeDetails(details);
		UsersDTO user=userService.getUsers(companyId,myDetails.get("email").toString());
		return ResponseEntity.ok(user);
	
	}
	@GetMapping(value="/getTechnicalUser/{companyId}")
	public ResponseEntity<List<UsersDTO>> getTechnicalUser(@PathVariable String companyId){
		
		List<UsersDTO> userDTOList=userService.getAllUsersByRole("TECHNICAL", companyId);
		return ResponseEntity.ok(userDTOList);
	
	}
	@GetMapping(value="/getUserDetails/{companyId}/{email}")
	public ResponseEntity<UsersDTO> getTechnicalUser(@PathVariable String companyId,@PathVariable String email){
		
		UsersDTO UserDTO=userService.getUsers(companyId, email);
		return ResponseEntity.ok(UserDTO);
	
	}
	@DeleteMapping(value="/deleteUserDetails/{companyId}/{email}")
	public void deleteUserDetails(@PathVariable String companyId,@PathVariable String email,@RequestHeader("Authorization") String authHeader) throws Exception{
//		System.out.print("-,,,,---,,,,---"+authHeader);
		userService.deleteUser(companyId, email, authHeader);
		
	
	}
	
	
}
