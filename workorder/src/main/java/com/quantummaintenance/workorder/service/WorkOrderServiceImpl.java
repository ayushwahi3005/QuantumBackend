package com.quantummaintenance.workorder.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;


import com.quantummaintenance.workorder.dto.*;
import com.quantummaintenance.workorder.entity.*;

import com.quantummaintenance.workorder.exception.ExtraFieldAlreadyPresentException;
import com.quantummaintenance.workorder.repository.ExtraFieldNameRepository;
import com.quantummaintenance.workorder.repository.ExtraFieldsRepository;
import com.quantummaintenance.workorder.repository.IdTableRepository;
import com.quantummaintenance.workorder.repository.MandatoryFieldsRepository;
import com.quantummaintenance.workorder.repository.ShowFieldsRepository;
import com.quantummaintenance.workorder.repository.WorkOrderRepository;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	@Autowired
	private ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	private MandatoryFieldsRepository mandatoryFieldsRepository;
	@Autowired
	private ShowFieldsRepository showFieldsRepository;
	
	@Autowired
	private IdTableRepository idTableRepository;
	
	@Autowired
	private ExtraFieldsRepository extraFieldsRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	@Override
	public WorkOrderDTO addOrder(WorkOrderDTO workOrderDTO) {
		// TODO Auto-generated method stub
		
		WorkOrder workOrder=modelMapper.map(workOrderDTO, WorkOrder.class);
		workOrder.setStatus(StatusEnum.open);
		workOrder.setLastUpdate(LocalDate.now());
		WorkOrderDTO myWorkOrderDTO=modelMapper.map(workOrderRepository.save(workOrder), WorkOrderDTO.class);
		return myWorkOrderDTO;
		
	}
	@Override
	public List<WorkOrderDTO> getOrderList(String companyId) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByCompanyId(companyId);
		//System.out.println("--------------------------------------->"+workOrderList.size());
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
	}
	@Override
	public void updateWorkOrder(WorkOrderDTO workOrderDTO) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(workOrderDTO.getId());
		WorkOrder workOrder= modelMapper.map(workOrderDTO, WorkOrder.class);
		workOrder.setLastUpdate(LocalDate.now());
		workOrderRepository.save(workOrder);
		
	}
	@Override
	public void deleteWorkOrder(String id) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(id);
		workOrderRepository.delete(workOrderOptional.get());
		
	}
	@Override
	public WorkOrderDTO getWorkOrder(String id) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(id);
		WorkOrderDTO workOrderDTO= modelMapper.map(workOrderOptional.get(), WorkOrderDTO.class);
		return workOrderDTO;
		
	}
	@Override
	public List<WorkOrderDTO> getWorkOrderFromAssetId(String assetId) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByAssetId(assetId);
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
	}
	@Override
	public List<WorkOrderDTO> searchedWorkOrder(String companyId,String data, String field) {
		// TODO Auto-generated method stub
		System.out.println("---------------------------------------------->"+field);
		List<WorkOrder> workOrderList=new ArrayList<>();
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
//		field=field.toLowerCase();
		if(field.equals("description")) {
			workOrderList=workOrderRepository.findByCompanyIdAndDescriptionLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("customer")) {
			workOrderList=workOrderRepository.findByCompanyIdAndCustomerLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("status")) {
			System.out.println("-----------------inside---------------------------->");
			workOrderList=workOrderRepository.findByCompanyIdAndStatusLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("priority")) {
			workOrderList=workOrderRepository.findByCompanyIdAndPriorityLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("dueDate")) {
			workOrderList=workOrderRepository.findByCompanyIdAndDueDateLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("assignedTechnician")) {
			workOrderList=workOrderRepository.findByCompanyIdAndAssignedTechnicianLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("assetDetails")) {
			workOrderList=workOrderRepository.findByCompanyIdAndAssetDetailsLikeIgnoreCase(companyId,data);
		}
		else if(field.equals("lastUpdate")) {
			workOrderList=workOrderRepository.findByCompanyIdAndDueDateLikeIgnoreCase(companyId,data);
		}
		workOrderList.stream().forEach((item)->{
			WorkOrderDTO workOrderDTO = modelMapper.map(item, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
			
		});
		return workOrderDTOList;
	}
	@Override
	public List<WorkOrderDTO> sortWorkOrder(String companyId, String field) {
		// TODO Auto-generated method stub
		
		System.out.println("---------------------------------------------->"+field);
		List<WorkOrder> workOrderList=new ArrayList<>();
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
//		field=field.toLowerCase();
		if(field.equals("description")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByCustomerAsc(companyId);
		}
		else if(field.equals("customer")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByDescriptionAsc(companyId);
		}
		else if(field.equals("status")) {
			System.out.println("-----------------insideSort---------------------------->");
			workOrderList=workOrderRepository.findByCompanyIdOrderByStatusAsc(companyId);
		}
		else if(field.equals("priority")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByPriorityAsc(companyId);
		}
		else if(field.equals("dueDate")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByDueDateAsc(companyId);
		}
		else if(field.equals("assignedTechnician")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByAssignedTechnicianAsc(companyId);
		}
		else if(field.equals("assetDetails")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByAssetDetailsAsc(companyId);
		}
		else if(field.equals("lastUpdate")) {
			workOrderList=workOrderRepository.findByCompanyIdOrderByLastUpdateAsc(companyId);
		}
		workOrderList.stream().forEach((item)->{
			WorkOrderDTO workOrderDTO = modelMapper.map(item, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
			
		});
		return workOrderDTOList;
	}
	@Override
	public void addAssetExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException {
		// TODO Auto-generated method stub
		ExtraFieldName extraFieldNameNew=extraFieldNameRepository.findByNameAndCompanyId(extraFieldNameDTO.getName().toLowerCase(),extraFieldNameDTO.getCompanyId());
		if(extraFieldNameNew!=null) {
			throw new ExtraFieldAlreadyPresentException("Extra Field Already Present");
		}
		extraFieldNameDTO.setName(extraFieldNameDTO.getName().toLowerCase());
		
		ExtraFieldName extraFieldName=modelMapper.map(extraFieldNameDTO, ExtraFieldName.class);
		extraFieldNameRepository.save(extraFieldName);
		
	}
	@Override
	public List<ExtraFieldNameDTO> getAssetExtraField(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFieldName> extraFieldNameList=extraFieldNameRepository.findByCompanyId(companyId);
		List<ExtraFieldNameDTO> extraFieldNameListDTO=new ArrayList<>();
		extraFieldNameList.stream().forEach((x)->{
			ExtraFieldNameDTO extraFieldNameDTO=modelMapper.map(x, ExtraFieldNameDTO.class);
			extraFieldNameListDTO.add(extraFieldNameDTO);
		});
		return extraFieldNameListDTO;
	}
	@Override
	public void deleteAssetExtraField(String id) {
		// TODO Auto-generated method stub
		Optional<ExtraFieldName> extraFieldNameOptional=extraFieldNameRepository.findById(id);
		extraFieldNameRepository.deleteById(id);
		ExtraFieldName extraFieldName=extraFieldNameOptional.get();
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByName(extraFieldName.getName().toLowerCase());
		extraFieldsList.stream().forEach((x)->{
			System.out.println("-------------------------------------->"+x.getName());
			extraFieldsRepository.delete(x);
		});
		
	}
	@Override
	public void updateShowFields(ShowFields showFields) {
		// TODO Auto-generated method stub
		Optional<ShowFields> showFieldsOptional=showFieldsRepository.findByNameAndCompanyId(showFields.getName(),showFields.getCompanyId());
		ShowFields myShowFields=new ShowFields();
		if(showFieldsOptional.isPresent()) {
			myShowFields=showFieldsOptional.get();
			showFields.setId(myShowFields.getId());
			
		}
		showFieldsRepository.save(showFields);
		
	}
	@Override
	public void updateMandatoryFields(MandatoryFields mandatoryFields) {
		// TODO Auto-generated method stub
		Optional<MandatoryFields> mandatoryFieldsOptional=mandatoryFieldsRepository.findByNameAndCompanyId(mandatoryFields.getName(),mandatoryFields.getCompanyId());
		MandatoryFields myMandatoryFields=new MandatoryFields();
		if(mandatoryFieldsOptional.isPresent()) {
			myMandatoryFields=mandatoryFieldsOptional.get();
			mandatoryFields.setId(myMandatoryFields.getId());
			
		}
		mandatoryFieldsRepository.save(mandatoryFields);
		
	}
	@Override
	public ShowFields getShowFields(String name, String companyId) {
		// TODO Auto-generated method stub
		Optional<ShowFields> showFieldsOptional=showFieldsRepository.findByNameAndCompanyId(name,companyId);
		if(showFieldsOptional.isPresent()) {
			return showFieldsOptional.get();
			}
			else {
				return null;
			}
	}
	@Override
	public MandatoryFields getMandatoryFields(String name, String companyId) {
		// TODO Auto-generated method stub
		Optional<MandatoryFields> mandatoryFieldsOptional=mandatoryFieldsRepository.findByNameAndCompanyId(name,companyId);
		if(mandatoryFieldsOptional.isPresent()) {
		return mandatoryFieldsOptional.get();
		}
		else {
			return null;
		}
	}
	@Override
	public List<ShowFields> getAllShowFields(String companyId) {
		// TODO Auto-generated method stub
		List<ShowFields> showFieldsList=showFieldsRepository.findByCompanyId(companyId);
		return showFieldsList;
	}
	@Override
	public List<MandatoryFields> getAllMandatoryFields(String companyId) {
		// TODO Auto-generated method stub
		List<MandatoryFields> mandatoryFieldsList=mandatoryFieldsRepository.findByCompanyId(companyId);
		return mandatoryFieldsList;
	}
	@Override
	public void deleteShowAndMandatoryFields(String companyId, String name) {
		// TODO Auto-generated method stub
		Optional<ShowFields> showFieldsOptional=showFieldsRepository.findByNameAndCompanyId(name, companyId);
		if(showFieldsOptional.isPresent()) {
		showFieldsRepository.delete(showFieldsOptional.get());
		}
		Optional<MandatoryFields> mandatoryFieldsOptional=mandatoryFieldsRepository.findByNameAndCompanyId(name, companyId);
		if(mandatoryFieldsOptional.isPresent()) {
		mandatoryFieldsRepository.delete(mandatoryFieldsOptional.get());
		}
		
	}
	@Override
	public Map<String, Map<String, String>> getextraFieldList(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldNameList=extraFieldsRepository.findByCompanyId(companyId);
		List<WorkOrder> assetList=workOrderRepository.findByCompanyId(companyId);
		Map<String, Map<String,String>> fieldNameValueMap=new HashMap<>();
		
		assetList.stream().forEach((workorder)->{
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((field)->{
				
				if(field.getWorkorderId().endsWith(workorder.getId()) ) {
					m.put(field.getName(), field.getValue());
					
				}
			
				
				
		
			});
			fieldNameValueMap.put(workorder.getId(), m);
		});
		return fieldNameValueMap;
	}
	@Override
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO) {
		// TODO Auto-generated method stub
		extraFieldsDTO.setName(extraFieldsDTO.getName().toLowerCase());

//		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByName(extraFieldsDTO.getName().toLowerCase());
//		if(!extraFieldsList.isEmpty()) {
//			throw new Exception("Extra Field Already Present");
//		}
		ExtraFields extraFields=modelMapper.map(extraFieldsDTO, ExtraFields.class);
		extraFieldsRepository.save(extraFields);
	
		
	}
	@Override
	public List<ExtraFieldsDTO> getExtraFields(String id) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByWorkorderId(id);
		if(extraFieldsList.isEmpty()) {
			return null;
		}
		List<ExtraFieldsDTO> extraFieldsDTOList=new ArrayList<>();
		extraFieldsList.stream().forEach((x)->{
			ExtraFieldsDTO extraFieldsDTO=modelMapper.map(x, ExtraFieldsDTO.class);
			extraFieldsDTOList.add(extraFieldsDTO);
		});
		return extraFieldsDTOList;
	}
	@Override
	public void deleteExtraFields(String id) throws Exception {
		// TODO Auto-generated method stub
		Optional<ExtraFields> extraFields=extraFieldsRepository.findById(id);
		if(extraFields.isEmpty()) {
			throw new Exception("No such extra Field");
		}
		extraFieldsRepository.delete(extraFields.get());
	}
	@Override
	public void deleteExtraFieldByWorkOrder(String workOrderId) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByWorkorderId(workOrderId);
		
		
		extraFieldsList.stream().forEach((x)->{
			
			extraFieldsRepository.delete(x);
		});
		
	}

}
