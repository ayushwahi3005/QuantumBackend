package com.quantummaintenance.workorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class MandatoryFields {

	@Id
	private String id;
	private String name;
	private boolean mandatory;
	private String email;
	private String type;
	private String companyId;
}
