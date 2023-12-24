package com.quantummaintenance.workorder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.workorder.entity.WorkOrder;

public interface WorkOrderRepository extends MongoRepository<WorkOrder,String> {
	public List<WorkOrder> findByEmail(String email);
	public List<WorkOrder> findByAssetId(String assetId);
}
