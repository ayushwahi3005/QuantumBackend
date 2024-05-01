package com.quantummaintenance.workorder.dto;



import java.time.LocalDate;
import java.util.Date;
import java.util.Map;

import com.quantummaintenance.workorder.entity.ExtraFields;

import lombok.Data;

@Data
public class WorkOrderDTO {
	private String id;
	private String description;
	private String customer;
	private String customerId;
	private StatusEnumDTO status;
	private PriorityEnumDTO priority;
	private LocalDate dueDate;
	private String assignedTechnician;
	private String assignedTechnicianId;
	private String assetDetails;
	private String assetId;
	private LocalDate lastUpdate;
	private String companyId;
//	private Map<String, ExtraFields> extraFields;
}
