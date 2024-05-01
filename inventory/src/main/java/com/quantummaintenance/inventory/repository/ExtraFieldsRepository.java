package com.quantummaintenance.inventory.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.inventory.entity.ExtraFields;





public interface ExtraFieldsRepository extends MongoRepository<ExtraFields,String> {
	public List<ExtraFields>findByInventoryId(String inventoryId);
	public List<ExtraFields> findByCompanyId(String companyId);
	public List<ExtraFields> findByName(String name);
	public ExtraFields findByNameAndInventoryId(String name,String inventoryId);

}
