package com.quantummaintenance.assests.dto;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CheckInOutDetailsDTO {
	private String status;
	private LocalDate date;
	private String employee;
	private String notes;
	private String location;
}
