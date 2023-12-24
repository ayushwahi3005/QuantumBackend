package com.quantummaintenance.assests.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.AssetFile;

public interface AssetFileRepository extends MongoRepository<AssetFile,String> {
	public List<AssetFile> findByAssetId(String assetId);
}
