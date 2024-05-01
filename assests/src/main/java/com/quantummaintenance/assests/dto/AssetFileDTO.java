package com.quantummaintenance.assests.dto;

import lombok.Data;

@Data
public class AssetFileDTO {
	private String id;
	private String assetId;
	private byte[] file;
	private String fileName;
}
