package com.quantummaintenance.users.service;

import java.util.List;

import com.quantummaintenance.users.dto.*;
import com.quantummaintenance.users.entity.*;

import io.jsonwebtoken.Claims;

public interface UserService {
	public void sendSimpleMessage(String to, String subject, String text);
	public AuthenticationResponseDTO generateToken(Mail mail);
	public Claims  decodeDetails(String token);
	public List<UsersDTO> getAllUsers(String companyId);
}
