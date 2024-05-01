package com.quantummaintenance.assests.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CheckInDTO {
	private String id;
	private String assetId;
	private String status;
	private LocalDate date;
	private String employee;
	private String notes;
	private String location;
	
}
