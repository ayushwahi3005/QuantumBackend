package com.quantummaintenance.assests.repository;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.ExtraFieldName;

public interface ExtraFieldNameRepository extends MongoRepository<ExtraFieldName,String>{
	public ExtraFieldName findByName(String name);
}
