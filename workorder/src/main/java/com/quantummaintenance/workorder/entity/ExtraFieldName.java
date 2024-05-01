package com.quantummaintenance.workorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class ExtraFieldName {
	
	@Id
	private String id;
	private String name;
	private String type;
	private String email;
	private String companyId;
}
