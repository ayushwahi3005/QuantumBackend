package com.quantummaintenance.companyCustomer.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class CompanyCustomerFile {

	@Id
	private String id;
	private String assetId;
	private String fileName;
	private byte[] file;
}
