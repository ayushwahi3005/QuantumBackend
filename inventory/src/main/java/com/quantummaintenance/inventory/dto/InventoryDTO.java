package com.quantummaintenance.inventory.dto;

import lombok.Data;

@Data
public class InventoryDTO {
	private String id;
	private String partId;
	private String partImage;
	private String partName;
	private Float price;
	private Float cost;
	private String category;
	private Integer quantity;
}
