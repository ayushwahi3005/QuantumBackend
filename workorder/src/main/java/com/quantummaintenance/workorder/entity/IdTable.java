package com.quantummaintenance.workorder.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class IdTable {

	@Id
	private String id;
	private int tableId;
	private String companyId;
	
	public void updateId() {
		this.tableId+=1;
	}
}
