package com.quantummaintenance.inventory.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.inventory.entity.Inventory;

public interface InventoryRepository extends MongoRepository<Inventory,String> {
	
}
