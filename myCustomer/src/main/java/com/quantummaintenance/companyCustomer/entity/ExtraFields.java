package com.quantummaintenance.companyCustomer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@Document
public class ExtraFields {
	

	@Id
	private String id;

	private String name;
	private String value;
	private String companyCustomerId;
	private String type;
	private String companyId;
	
	
}
