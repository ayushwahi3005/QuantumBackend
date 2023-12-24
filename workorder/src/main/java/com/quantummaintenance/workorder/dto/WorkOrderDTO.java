package com.quantummaintenance.workorder.dto;



import java.time.LocalDate;
import java.util.Date;



import lombok.Data;

@Data
public class WorkOrderDTO {
	private String id;
	private String description;
	private String customer;
	private StatusEnumDTO status;
	private PriorityEnumDTO priority;
	private Date dueDate;
	private String assignedTechnician;
	private String assetDetails;
	private String assetId;
	private LocalDate lastUpdate;
	private String email;
}
