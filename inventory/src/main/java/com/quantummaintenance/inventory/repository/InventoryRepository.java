package com.quantummaintenance.inventory.repository;

import java.util.List;


import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.inventory.entity.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory,String> {
	public List<Inventory> findByCompanyId(String companyId);
	public  Inventory  findByInventoryIdAndCompanyId(Integer inventoryId,String companyId);
	
}
