package com.quantummaintenance.users.service;

import java.util.List;

import com.quantummaintenance.users.dto.*;
import com.quantummaintenance.users.entity.*;
import com.quantummaintenance.users.exception.UserException;

import io.jsonwebtoken.Claims;

public interface UserService {
	
	
	public void sendSimpleMessage(String to, String subject, String text);
	public AuthenticationResponseDTO generateToken(Mail mail);
	public Claims  decodeDetails(String token);
	public List<UsersDTO> getAllUsers(String companyId);
	public void registerUser(Users user) throws UserException;
	public UsersDTO getUsers(String companyId,String email);
	public List<UsersDTO> getAllUsersByRole(String role,String companyId);
}
