package com.quantummaintenance.workorder.service;

import java.util.List;
import java.util.Map;

import com.quantummaintenance.workorder.dto.*;
import com.quantummaintenance.workorder.entity.MandatoryFields;
import com.quantummaintenance.workorder.entity.ShowFields;
import  com.quantummaintenance.workorder.exception.*;

public interface WorkOrderService {
	public WorkOrderDTO addOrder(WorkOrderDTO workOrderDTO);
	public List<WorkOrderDTO> getOrderList(String companyId);
	public WorkOrderDTO getWorkOrder(String id);
	public void updateWorkOrder(WorkOrderDTO workOrderDTO);
	public void deleteWorkOrder(String id);
	public List<WorkOrderDTO> getWorkOrderFromAssetId(String assetId);
	public  List<WorkOrderDTO> searchedWorkOrder(String companyId,String data,String field);
	public  List<WorkOrderDTO> sortWorkOrder(String companyId,String field);
	public void addAssetExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException;
	public List<ExtraFieldNameDTO> getAssetExtraField(String companyId);
	public void deleteAssetExtraField(String id);
	public void updateShowFields(ShowFields showFields);
	public void updateMandatoryFields(MandatoryFields mandatoryFields);
	public ShowFields getShowFields(String name,String companyId);
	public MandatoryFields getMandatoryFields(String name,String companyId);
	public List<ShowFields> getAllShowFields(String companyId);
	public List<MandatoryFields> getAllMandatoryFields(String companyId);
	public void deleteShowAndMandatoryFields(String companyId,String name);
	public Map<String, Map<String,String>>getextraFieldList(String companyId);
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO);
	public List<ExtraFieldsDTO> getExtraFields(String id);
	public void deleteExtraFields(String id) throws Exception;
	public void deleteExtraFieldByWorkOrder(String workOrderId);
	
	
}
