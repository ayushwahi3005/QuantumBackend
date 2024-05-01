package com.quantummaintenance.assests.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.CheckInOut;

public interface CheckInOutRepository extends MongoRepository<CheckInOut,String> {
			public List<CheckInOut> findByAssetId(String assetid);
}
