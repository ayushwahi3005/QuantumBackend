package com.quantummaintenance.assests.entity;

import org.springframework.data.annotation.Id;

import lombok.Data;

@Data
public class ExtraFields {
	@Id
	private String id;
	private String email;
	private String name;
	private String value;
	private String assetId;
	private String type;
}
