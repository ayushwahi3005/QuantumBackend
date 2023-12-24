package com.quantummaintenance.assests.dto;


import java.util.List;



import lombok.Data;

@Data
public class CheckInOutDTO {

	private String id;
	private String assetId;
	private String status;
	private List<CheckInOutDetailsDTO> detailsList;
}
