package com.quantummaintenance.inventory.service;

import java.util.List;

import com.quantummaintenance.inventory.dto.InventoryDTO;

public interface InventoryService {
	public void addInventory(InventoryDTO inventoryDTO);
	public List<InventoryDTO> getAllInventory(String companyId);
	public InventoryDTO getInventory(String id);
	public void deleteInventory(String id);
}
