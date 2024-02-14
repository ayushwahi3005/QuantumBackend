package com.quantummaintenance.workorder.entity;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Document
@Data
public class WorkOrder {
	
	@Id
	private String id;
	private String description;
	private String customer;
	private StatusEnum status;
	private PriorityEnum priority;
	private Date dueDate;
	private String assignedTechnician;
	private String assetDetails;
	private String assetId;
	private LocalDate lastUpdate;
	private String companyId;
//	private Map<String, ExtraFields> extraFields;
	
	
}
