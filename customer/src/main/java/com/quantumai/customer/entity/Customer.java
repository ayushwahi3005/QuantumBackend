package com.quantumai.customer.entity;

import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
//@Document(collection="customer")
@Document
public class Customer {
	private String firstName;
	private String lastName;
	private String email;
	private String companyName;
	private String mobileNumber;
	
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	
	
}
