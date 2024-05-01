package com.quantummaintenance.users.dto;





import com.quantummaintenance.users.entity.Role;

import lombok.Data;

@Data
public class CustomerDTO {
	private String firstName;
	private String lastName;
	private String email;
	private String companyName;
	private String mobileNumber;
	private String password;
	private String role;
	
	
	
}
