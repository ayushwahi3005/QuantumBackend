package com.quantummaintenance.assests.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.ExtraFieldName;

public interface ExtraFieldNameRepository extends MongoRepository<ExtraFieldName,String>{
	public ExtraFieldName findByNameAndCompanyId(String name,String companyId);
	public List<ExtraFieldName> findByCompanyId(String companyId);
}
