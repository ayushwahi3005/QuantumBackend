package com.quantummaintenance.workorder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.workorder.entity.ExtraFieldName;

public interface ExtraFieldNameRepository extends MongoRepository<ExtraFieldName,String>{
	public ExtraFieldName findByName(String name);
	public List<ExtraFieldName> findByCompanyId(String companyId);
	public ExtraFieldName findByNameAndCompanyId(String name,String companyId);
}
