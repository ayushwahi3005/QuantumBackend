package com.quantummaintenance.users.dto;

import com.quantummaintenance.users.entity.Role;

import lombok.Data;
@Data
public class UsersDTO {
	private String id;
	private String firstName;
	private String lastName;
	private String email;
	private String companyId;
	private String mobileNumber;
	private String password;
	private String title;
	private String role;
}
