package com.quantummaintenance.companyCustomer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;



import lombok.Data;

@Document
@Data
public class CompanyCustomer {
	
	@Id
	private String id;
	private Integer companyCustomerId;
	private String name;
	private String companyId;
	private String category;
	private String status;
	private Long phone;
	private String email;
	private String address;
	private String apartment;
	private String city;
	private String state;
	private Integer zipCode;
	

}
