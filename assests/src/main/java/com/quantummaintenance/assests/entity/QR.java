package com.quantummaintenance.assests.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class QR {

	@Id
	private String id;
	private String type;
	private String custom;
	private String optional;
	private String companyId;
	
}
