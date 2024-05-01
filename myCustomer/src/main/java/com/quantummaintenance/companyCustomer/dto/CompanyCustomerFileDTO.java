package com.quantummaintenance.companyCustomer.dto;

import lombok.Data;

@Data
public class CompanyCustomerFileDTO {
	private String id;
	private String companyCustomerId;
	private String fileName;
	private byte[] file;
}
