package com.quantummaintenance.users.dto;

import com.quantummaintenance.users.entity.Role;

import lombok.Data;
@Data
public class UsersDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String companyId;
	private String mobileNumber;
	private String password;
	private Role role;
}