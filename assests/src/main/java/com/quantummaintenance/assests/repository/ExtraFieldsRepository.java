package com.quantummaintenance.assests.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.ExtraFields;

public interface ExtraFieldsRepository extends MongoRepository<ExtraFields,String> {
	public List<ExtraFields>findByAssetId(String assetId);
	public List<ExtraFields> findByCompanyId(String companyId);
	public List<ExtraFields> findByName(String name);
	public Optional<ExtraFields> findByNameAndAssetId(String name,String assetId);

}
