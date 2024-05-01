package com.quantummaintenance.inventory.dto;

import lombok.Data;

@Data
public class ExtraFieldsDTO {
	private String id;

	private String name;
	private String value;
	private String inventoryId;
	private String type;
	private String companyId;
}
