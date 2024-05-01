package com.quantummaintenance.workorder.dto;

import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import lombok.Data;

@Data
public class WorkOrderWithExtraFieldsDTO {
	private String id;
	private String description;
	private String customer;
	private StatusEnumDTO status;
	private PriorityEnumDTO priority;
	private Date dueDate;
	private String assignedTechnician;
	private String assetDetails;
	private String assetId;
//	private LocalDate lastUpdate;
	private String companyId;
	private Map<String,String> extra;

}
