package com.quantummaintenance.inventory.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.inventory.entity.ShowFields;





public interface ShowFieldsRepository extends MongoRepository<ShowFields,String> {
	public Optional<ShowFields> findByNameAndCompanyId(String name,String companyId);
	public List<ShowFields> findByCompanyId(String companyId);
}
