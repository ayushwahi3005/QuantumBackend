package com.quantummaintenance.users.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class Users {
	
	@Id
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String companyId;
	private String mobileNumber;
	private String password;
	private Role role;
}
