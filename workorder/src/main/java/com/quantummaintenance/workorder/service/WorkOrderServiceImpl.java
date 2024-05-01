package com.quantummaintenance.workorder.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

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
	public List<String> searchedWorkOrder(String companyId,String data, String field) {
		
		
		
		List<Map<String,String> > mapList=new ArrayList<>();
		List<String> myList=workOrderAllData(companyId);
		ObjectMapper objectMapper = new ObjectMapper();
		myList.stream().forEach((workorder)->{
			Map<String,String> m=new HashMap<>();
			 try {
		            // Convert JSON string to Map<String, String>
		            m= objectMapper.readValue(workorder, new TypeReference<Map<String, String>>() {});
		            mapList.add(m);
		           
		        } catch (Exception e) {
		            // Handle exception
		            e.printStackTrace();
		          
		        }
		});
//		System.out.println(field);
		List<Map<String, String>> res= mapList.stream().filter(m->{
			System.out.println("---->"+m.get(field));
			String s=m.get(field);
//			return s.contains(data);
			return s.toLowerCase().contains(data.toLowerCase());
				
			
		
			}).collect(Collectors.toList());
		 System.out.println(res);
		 System.out.println(res.size());
		 List<String> resList=new ArrayList<>();
		 res.stream().forEach((mydata)->{
			 try {
//	            // Convert POJO to JSON string
	          String json = objectMapper.writeValueAsString(mydata);
	          
	          resList.add(json);
	           System.out.print(json);

	          
	            
	       } catch (Exception e) {
	            e.printStackTrace();
	          
	           
	        }
		 });
		 return resList;
		
		
		
		
		
		// TODO Auto-generated method stub
//		System.out.println("---------------------------------------------->"+field);
//		List<WorkOrder> workOrderList=new ArrayList<>();
//		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
//		
//		
////		field=field.toLowerCase();
//		if(field.equals("description")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndDescriptionLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("customer")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndCustomerLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("status")) {
//			System.out.println("-----------------inside---------------------------->");
//			workOrderList=workOrderRepository.findByCompanyIdAndStatusLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("priority")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndPriorityLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("dueDate")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndDueDateLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("assignedTechnician")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndAssignedTechnicianLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("assetDetails")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndAssetDetailsLikeIgnoreCase(companyId,data);
//		}
//		else if(field.equals("lastUpdate")) {
//			workOrderList=workOrderRepository.findByCompanyIdAndDueDateLikeIgnoreCase(companyId,data);
//		}
//		workOrderList.stream().forEach((item)->{
//			WorkOrderDTO workOrderDTO = modelMapper.map(item, WorkOrderDTO.class);
//			workOrderDTOList.add(workOrderDTO);
//			
//		});
//		return workOrderDTOList;
	}
	@Override
	public List<String> sortWorkOrder(String companyId, String field) {
		
		ExtraFieldName extraFieldName=extraFieldNameRepository.findByNameAndCompanyId(field, companyId);
		System.out.println(extraFieldName);
		// TODO Auto-generated method stub
//		String type= extraFieldName.getType();
		List<Map<String,String> > mapList=new ArrayList<>();
		List<String> myList=workOrderAllData(companyId);
		ObjectMapper objectMapper = new ObjectMapper();
		myList.stream().forEach((workorder)->{
			Map<String,String> m=new HashMap<>();
			 try {
		            // Convert JSON string to Map<String, String>
		            m= objectMapper.readValue(workorder, new TypeReference<Map<String, String>>() {});
		            mapList.add(m);
		           
		        } catch (Exception e) {
		            // Handle exception
		            e.printStackTrace();
		          
		        }
		});
//		System.out.println(field);
		Comparator<Map<String, String>> customComparator=null;
//		if(type=="number") {
//			customComparator = Comparator.comparing(m -> Integer.parseInt(m.get(field)));
//		}
//		else {
			customComparator = Comparator.comparing(m -> m.get(field));
//		}
		

		
		List<Map<String, String>> res= mapList.stream().sorted(customComparator).collect(Collectors.toList());
		 System.out.println(res);
		 System.out.println(res.size());
		 List<String> resList=new ArrayList<>();
		 res.stream().forEach((mydata)->{
			 try {
//	            // Convert POJO to JSON string
	          String json = objectMapper.writeValueAsString(mydata);
	          
	          resList.add(json);
	           System.out.print(json);

	          
	            
	       } catch (Exception e) {
	            e.printStackTrace();
	          
	           
	        }
		 });
		 return resList;
		
//		System.out.println("---------------------------------------------->"+field);
//		List<WorkOrder> workOrderList=new ArrayList<>();
//		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
//
//		if(field.equals("description")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByCustomerAsc(companyId);
//		}
//		else if(field.equals("customer")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByDescriptionAsc(companyId);
//		}
//		else if(field.equals("status")) {
//			System.out.println("-----------------insideSort---------------------------->");
//			workOrderList=workOrderRepository.findByCompanyIdOrderByStatusAsc(companyId);
//		}
//		else if(field.equals("priority")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByPriorityAsc(companyId);
//		}
//		else if(field.equals("dueDate")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByDueDateAsc(companyId);
//		}
//		else if(field.equals("assignedTechnician")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByAssignedTechnicianAsc(companyId);
//		}
//		else if(field.equals("assetDetails")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByAssetDetailsAsc(companyId);
//		}
//		else if(field.equals("lastUpdate")) {
//			workOrderList=workOrderRepository.findByCompanyIdOrderByLastUpdateAsc(companyId);
//		}
//		workOrderList.stream().forEach((item)->{
//			WorkOrderDTO workOrderDTO = modelMapper.map(item, WorkOrderDTO.class);
//			workOrderDTOList.add(workOrderDTO);
//			
//		});
//		return workOrderDTOList;
	}
	@Override
	public void addWorkOrderExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException {
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
	public List<ExtraFieldNameDTO> getWorkOrderExtraField(String companyId) {
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
	public void deleteWorkOrderExtraField(String id) {
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
	@Override
	public List<String> workOrderAllData(String companyId) {
		// TODO Auto-generated method stub
		
		List<ExtraFieldName> extraFieldNameList=extraFieldNameRepository.findByCompanyId(companyId);
//		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(workOrderId);
		List<WorkOrder> workOrderList= workOrderRepository.findByCompanyId(companyId);
//		WorkOrder workOrder=workOrderOptional.get();
//		WorkOrderWithExtraFieldsDTO workOrderWithExtraFieldsDTO = modelMapper.map(workOrder, WorkOrderWithExtraFieldsDTO.class);
		
		
		List<String> mapList=new ArrayList<>();
		workOrderList.stream().forEach((order)->{
			List<ExtraFields> extraFieldsList=extraFieldsRepository.findByWorkorderId(order.getId());
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((x)->{
				m.put(x.getName(), "");
				extraFieldsList.stream().forEach((x1)->{
					m.put(x1.getName(), x1.getValue());
				});
			});
			m.put("id", order.getId());
			m.put("assetDetails",order.getAssetDetails());
			m.put("assignedTechnician",order.getAssignedTechnician());
			m.put("assetId",order.getAssetId());
			m.put("companyId",order.getCompanyId());
			m.put("customer",order.getCustomer());
			m.put("customerId",order.getCustomerId());
			m.put("description",order.getDescription());
			if(order.getDueDate()!=null) m.put("dueDate",order.getDueDate().toString());
			if(order.getLastUpdate()!=null) m.put("lastUpdate",order.getLastUpdate().toString());
			if(order.getPriority()!=null) m.put("priority",order.getPriority().toString());
			if(order.getStatus()!=null) m.put("status",order.getStatus().toString());
			ObjectMapper objectMapper = new ObjectMapper();
			try {
	            // Convert POJO to JSON string
	            String json = objectMapper.writeValueAsString(m);
	           
	            mapList.add(json);
//	            System.out.print(json);
//	            System.out.print(m);
	           
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	           
	            
	        }
			
			
		});
		

		return mapList;
		
		
	}
	@Override
	public List<WorkOrderDTO> getWorkOrderByTechnicianId(String companyId,String id) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByCompanyIdAndAssignedTechnicianId(companyId,id);
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
		
	}
	@Override
	public List<WorkOrderDTO> getWorkOrderByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByCustomerId(customerId);
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
	}
	@Override
	public void updateWorkOrdersWithClosed(String customerId) {
		// TODO Auto-generated method stub
		List<WorkOrder> assetsList=workOrderRepository.findByCustomerId(customerId);
		
		assetsList.stream().forEach(x->{
			x.setStatus(StatusEnum.closed);
			workOrderRepository.save(x);
			
		});
		
	}

}
