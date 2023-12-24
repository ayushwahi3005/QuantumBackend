package com.quantummaintenance.assests.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Assets {
	
	private String email;
	@Id
	private String id;
	private String name;
	private String serialNumber;
	private String category;
	private String customer;
	private String location;
	private String status;
	private String image;
}
