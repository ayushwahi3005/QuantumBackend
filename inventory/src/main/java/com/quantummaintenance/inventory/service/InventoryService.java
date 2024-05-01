package com.quantummaintenance.inventory.service;

import java.util.List;
import java.util.Map;

import com.quantummaintenance.inventory.dto.ExtraFieldNameDTO;
import com.quantummaintenance.inventory.dto.ExtraFieldsDTO;
import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.entity.Inventory;
import com.quantummaintenance.inventory.entity.MandatoryFields;
import com.quantummaintenance.inventory.entity.ShowFields;


public interface InventoryService {
	public Inventory addInventory(InventoryDTO inventoryDTO);
	public void updateInventory(InventoryDTO inventoryDTO);
	public List<InventoryDTO> getAllInventory(String companyId);
	public InventoryDTO getInventory(String id);
	public void deleteInventory(String id);
	
	public void addInventoryExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws Exception;
	public List<ExtraFieldNameDTO> getInventoryExtraField(String companyId);
	public void deleteInventoryExtraField(String id);
	public void updateShowFields(ShowFields showFields);
	public void updateMandatoryFields(MandatoryFields mandatoryFields);
	public ShowFields getShowFields(String name,String companyId);
	public MandatoryFields getMandatoryFields(String name,String companyId);
	public List<ShowFields> getAllShowFields(String companyId);
	public List<MandatoryFields> getAllMandatoryFields(String companyId);
	public void deleteShowAndMandatoryFields(String companyId,String name);
	public Map<String, Map<String,String>>getExtraFieldList(String companyId);
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO);
	public List<ExtraFieldsDTO> getExtraFields(String id);
	public void deleteExtraFields(String id) throws Exception;
	public void deleteExtraFieldByInventory(String workOrderId);
	public List<String> inventoryAllData(String companyId);
}
