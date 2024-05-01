package com.quantummaintenance.inventory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class ShowFields {

	@Id
	private String id;
	private String name;
	private boolean show;
	private String email;
	private String type;
	private String companyId;
}
