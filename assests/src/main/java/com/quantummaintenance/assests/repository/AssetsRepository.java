package com.quantummaintenance.assests.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.Assets;

public interface AssetsRepository extends MongoRepository<Assets,String> {
	List<Assets> findByCompanyId(String companyId);
	Assets findByAssetIdAndCompanyId(Integer assetId,String companyId);
}
