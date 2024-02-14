package com.quantummaintenance.assests.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

import org.springframework.web.multipart.MultipartFile;

import com.quantummaintenance.assests.dto.AssetFileDTO;
import com.quantummaintenance.assests.dto.AssetImageDTO;
import com.quantummaintenance.assests.dto.AssetsDTO;
import com.quantummaintenance.assests.dto.CheckInDTO;
import com.quantummaintenance.assests.dto.CheckInOutDTO;
import com.quantummaintenance.assests.dto.ExtraFieldNameDTO;
import com.quantummaintenance.assests.dto.ExtraFieldsDTO;
import com.quantummaintenance.assests.entity.AssetFile;
import com.quantummaintenance.assests.entity.Assets;
import com.quantummaintenance.assests.entity.MandatoryFields;
import com.quantummaintenance.assests.entity.ShowFields;
import com.quantummaintenance.assests.exception.ExtraFieldAlreadyPresentException;

public interface AssetsService {
	public List<AssetsDTO> getAssetsDetails(String companyId);
	public AssetsDTO addAssets(AssetsDTO assetsDTO);
	public void importExcel(List<AssetsDTO> assetsDTOList,Map<String,String> columnMap);
	public void addImage(AssetImageDTO assetImageDTO) throws Exception;
	public void removeImage(String id) throws Exception;
	public void removeAsset(String id) throws Exception;
	public AssetsDTO getAsset(String assetId) throws Exception;
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO) throws Exception;
	public List<ExtraFieldsDTO> getExtraFields(String id);
	public void deleteExtraFields(String id) throws Exception;
	public List<ExtraFieldNameDTO> getAssetExtraField(String companyId);
	public void addAssetExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException;
	public void deleteAssetExtraField(String id);
	public Map<String, Map<String,String>>getextraFieldList(String companyId);
	public void addCheckInOut(CheckInDTO checkInDTO);
	public List<CheckInOutDTO> getCheckOutInList(String assetId);
	public AssetFile addAssetFile(MultipartFile file,String assetId) throws IOException;
	public List<AssetFileDTO> getAssetFile(String assetId);
	public AssetFileDTO downloadFile(String id);
	public void deleteFile(String id);
	public void updateShowFields(ShowFields showFields);
	public void updateMandatoryFields(MandatoryFields mandatoryFields);
	public ShowFields getShowFields(String name,String companyId);
	public MandatoryFields getMandatoryFields(String name,String companyId);
	public List<ShowFields> getAllShowFields(String companyId);
	public List<MandatoryFields> getAllMandatoryFields(String companyId);
	public void deleteShowAndMandatoryFields(String companyId,String name);
	public void updateAssetWithFile(List<AssetsDTO> assetsDTOList,String companyId);
}
