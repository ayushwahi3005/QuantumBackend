package com.quantummaintenance.workorder.dto;

import lombok.Data;

@Data
public class ExtraFieldsDTO {
	private String id;

	private String name;
	private String value;
	private String workorderId;
	private String type;
	private String companyId;
}
