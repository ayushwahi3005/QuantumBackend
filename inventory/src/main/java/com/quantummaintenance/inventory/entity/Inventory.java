package com.quantummaintenance.inventory.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class Inventory {
	@Id
	private String id;
	private String partId;
	private String partImage;
	private String partName;
	private Float price;
	private Float cost;
	private String category;
	private Integer quantity;
	
	
}
