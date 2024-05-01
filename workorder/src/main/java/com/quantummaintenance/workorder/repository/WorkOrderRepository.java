package com.quantummaintenance.workorder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.workorder.entity.WorkOrder;

public interface WorkOrderRepository extends MongoRepository<WorkOrder,String> {
	public List<WorkOrder> findByCompanyId(String companyId);
	public List<WorkOrder> findByCustomerId(String customerId);
	public List<WorkOrder> findByAssetId(String assetId);
	public List<WorkOrder> findByCompanyIdAndCustomerLikeIgnoreCase(String companyId,String customer);
	public List<WorkOrder> findByCompanyIdAndDescriptionLikeIgnoreCase(String companyId,String description);
	public List<WorkOrder> findByCompanyIdAndStatusLikeIgnoreCase(String companyId,String status);
	public List<WorkOrder> findByCompanyIdAndPriorityLikeIgnoreCase(String companyId,String priority);
	public List<WorkOrder> findByCompanyIdAndDueDateLikeIgnoreCase(String companyId,String dueDate);
	public List<WorkOrder> findByCompanyIdAndAssignedTechnicianLikeIgnoreCase(String companyId,String assignedTechnician);
	public List<WorkOrder> findByCompanyIdAndAssetDetailsLikeIgnoreCase(String companyId,String assetDetails);
	public List<WorkOrder> findByCompanyIdAndLastUpdateLikeIgnoreCase(String companyId,String lastUpdate);
	
	public List<WorkOrder> findByCompanyIdOrderByCustomerAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByDescriptionAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByStatusAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByPriorityAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByDueDateAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByAssignedTechnicianAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByAssetDetailsAsc(String companyId);
	public List<WorkOrder> findByCompanyIdOrderByLastUpdateAsc(String companyId);
	public List<WorkOrder> findByCompanyIdAndAssignedTechnicianId(String companyId,String assignedTechnicianId);
}
