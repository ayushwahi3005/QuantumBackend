package com.quantummaintenance.assests.entity;

import java.time.LocalDate;

import lombok.Data;

@Data
public class CheckInOutDetails {
	private String status;
	private LocalDate date;
	private String employee;
	private String notes;
	private String location;
}
