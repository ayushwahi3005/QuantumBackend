package com.quantummaintenance.assests.entity;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class Assets {
	
	@Id
	private String id;
	private String email;
	private Integer assetId;
	private String name;
	private String serialNumber;
	private String category;
	private String customer;
	private String customerId;
	private String location;
	private String status;
	private String image;
	private String companyId;
}
