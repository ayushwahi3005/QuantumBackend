package com.quantummaintenance.assests.entity;


import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.Data;

@Data
@Document
public class CheckInOut {

	@Id
	private String id;
	private String assetId;
	private String status;
	private List<CheckInOutDetails> detailsList;
}
