package com.quantummaintenance.assests.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.ExtraFields;

public interface ExtraFieldsRepository extends MongoRepository<ExtraFields,String> {
	public List<ExtraFields>findByAssetId(String assetId);
	public List<ExtraFields> findByEmail(String email);
	public List<ExtraFields> findByName(String name);

}
