package com.quantummaintenance.assests.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.web.multipart.MultipartFile;

import com.quantummaintenance.assests.dto.AssetFileDTO;
import com.quantummaintenance.assests.dto.AssetImageDTO;
import com.quantummaintenance.assests.dto.AssetsDTO;
import com.quantummaintenance.assests.dto.CheckInDTO;
import com.quantummaintenance.assests.dto.CheckInOutDTO;
import com.quantummaintenance.assests.dto.ExtraFieldNameDTO;
import com.quantummaintenance.assests.dto.ExtraFieldsDTO;
import com.quantummaintenance.assests.entity.AssetFile;

public interface AssetsService {
	public List<AssetsDTO> getAssetsDetails(String email);
	public void addAssets(AssetsDTO assetsDTO);
	public void importExcel(List<AssetsDTO> assetsDTOList);
	public void addImage(AssetImageDTO assetImageDTO) throws Exception;
	public void removeImage(String id) throws Exception;
	public void removeAsset(String id) throws Exception;
	public AssetsDTO getAsset(String assetId) throws Exception;
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO) throws Exception;
	public List<ExtraFieldsDTO> getExtraFields(String id);
	public void deleteExtraFields(String id) throws Exception;
	public List<ExtraFieldNameDTO> getAssetExtraField(String email);
	public void addAssetExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws Exception;
	public void deleteAssetExtraField(String id);
	public Map<String, Map<String,String>>getextraFieldList(String email);
	public void addCheckInOut(CheckInDTO checkInDTO);
	public List<CheckInOutDTO> getCheckOutInList(String assetId);
	public AssetFile addAssetFile(MultipartFile file,String assetId) throws IOException;
	public List<AssetFileDTO> getAssetFile(String assetId);
	public AssetFileDTO downloadFile(String id);
	public void deleteFile(String id);
}
