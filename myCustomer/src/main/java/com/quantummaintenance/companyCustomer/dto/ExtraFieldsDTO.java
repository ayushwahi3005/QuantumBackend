package com.quantummaintenance.companyCustomer.dto;

import lombok.Data;

@Data
public class ExtraFieldsDTO {
	private String id;

	private String name;
	private String value;
	private String companyCustomerId;
	private String type;
	private String companyId;
}
