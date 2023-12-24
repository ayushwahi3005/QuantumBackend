package com.quantummaintenance.workorder.service;

import java.util.List;

import com.quantummaintenance.workorder.dto.WorkOrderDTO;

public interface WorkOrderService {
	public void addOrder(WorkOrderDTO workOrderDTO);
	public List<WorkOrderDTO> getOrderList(String userId);
	public WorkOrderDTO getWorkOrder(String id);
	public void updateWorkOrder(WorkOrderDTO workOrderDTO);
	public void deleteWorkOrder(String id);
	public List<WorkOrderDTO> getWorkOrderFromAssetId(String assetId);
	
}
