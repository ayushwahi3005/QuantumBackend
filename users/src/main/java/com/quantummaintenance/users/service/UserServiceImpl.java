package com.quantummaintenance.users.service;
import java.security.Key;
import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.quantummaintenance.users.dto.AuthenticationResponseDTO;
import com.quantummaintenance.users.dto.MailDTO;
import com.quantummaintenance.users.dto.UsersDTO;
import com.quantummaintenance.users.security.JwtService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import com.quantummaintenance.users.entity.*;
import com.quantummaintenance.users.exception.UserException;
import com.quantummaintenance.users.repository.UsersRepository;
@Service
public class UserServiceImpl implements UserService{
	
	@Autowired
	private JavaMailSender emailSender;
	@Autowired JwtService jwtService;
	
	@Autowired
	private PasswordEncoder passwordEncoder ;
	
	
	private ModelMapper modelMapper=new ModelMapper();

	@Autowired private UsersRepository usersRepository;
	
	 @Value("${application.security.jwt.secret-key}")
	  private String secretKey;
	 
	@Override
	public void sendSimpleMessage(String to, String subject, String text) {
		// TODO Auto-generated method stub
		SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(to);
        message.setSubject(subject);
        message.setText(text);
        emailSender.send(message);
	}


	@Override
	public AuthenticationResponseDTO generateToken(Mail mail) {


		
		var token=jwtService.generateTokenForInvite(mail);
		System.out.println("--------------------------------token created------------------------------------------------->"+token);
		return AuthenticationResponseDTO.builder().token(token).build();
		
		
	}

	
	@Override
	public Claims decodeDetails(String token) {
		// TODO Auto-generated method stub
		byte[] keyBytes = Decoders.BASE64.decode(secretKey);
		return Jwts.parser()
                .setSigningKey(Keys.hmacShaKeyFor(keyBytes))
                .parseClaimsJws(token)
                .getBody();
	}


	@Override
	public List<UsersDTO> getAllUsers(String companyId) {
		// TODO Auto-generated method stub
		List<Users> usersList=usersRepository.findByCompanyId(companyId);
		List<UsersDTO> usersListDTO=new ArrayList<>();
		usersList.stream().forEach((user)->{
			UsersDTO usersDTO=modelMapper.map(user, UsersDTO.class);
			usersListDTO.add(usersDTO);
		});
		return usersListDTO;
	}


	@Override
	public void registerUser(Users user) throws UserException {
		// TODO Auto-generated method stub
		Optional<Users> OptionalUser=usersRepository.findByCompanyIdAndEmail(user.getCompanyId(), user.getEmail());
		if(OptionalUser.isEmpty()) {
			usersRepository.save(user);
		}
		else {
			throw  new UserException("User already Invited");
		}
			
	
		
		
	}


	@Override
	public UsersDTO getUsers(String companyId, String email) {
		// TODO Auto-generated method stub
		Optional<Users> Optionaluser=usersRepository.findByCompanyIdAndEmail(companyId, email);
		UsersDTO usersDTO=modelMapper.map(Optionaluser.get(), UsersDTO.class);
		return usersDTO;
	}


	@Override
	public List<UsersDTO> getAllUsersByRole(String role,String companyId) {
		// TODO Auto-generated method stub
		List<Users> usersList=usersRepository.findByRoleEndingWithAndCompanyId(role, companyId);
		List<UsersDTO> usersListDTO=new ArrayList<>();
		usersList.stream().forEach((user)->{
			UsersDTO usersDTO=modelMapper.map(user, UsersDTO.class);
			usersListDTO.add(usersDTO);
		});
		return usersListDTO;
	}
	



}
