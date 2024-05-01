package com.quantummaintenance.users.dto;

import com.quantummaintenance.users.entity.Role;

import lombok.Data;

@Data
public class MailDTO {

	private String firstName;
	private String lastName;
	private String phoneNumber;
	private String message;
	private String email;
	private Role role;
}
