package com.quantummaintenance.inventory.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ArrayList;
import java.util.HashMap;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantummaintenance.inventory.dto.ExtraFieldNameDTO;
import com.quantummaintenance.inventory.dto.ExtraFieldsDTO;
import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.entity.*;

import com.quantummaintenance.inventory.repository.ExtraFieldNameRepository;
import com.quantummaintenance.inventory.repository.ExtraFieldsRepository;
import com.quantummaintenance.inventory.repository.InventoryRepository;
import com.quantummaintenance.inventory.repository.MandatoryFieldsRepository;
import com.quantummaintenance.inventory.repository.ShowFieldsRepository;



@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryRepository inventoryRepository;
	@Autowired
	ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	ExtraFieldsRepository extraFieldsRepository;
	@Autowired
	MandatoryFieldsRepository mandatoryFieldsRepository;
	@Autowired
	ShowFieldsRepository showFieldsRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	@Override
	public Inventory addInventory(InventoryDTO inventoryDTO) {
		// TODO Auto-generated method stub
		Inventory inventory=modelMapper.map(inventoryDTO, Inventory.class);
		return inventoryRepository.save(inventory);
		
	}
	@Override
	public List<InventoryDTO> getAllInventory(String companyId) {
		// TODO Auto-generated method stub
		List<InventoryDTO> inventoryListDTO=new ArrayList<>();
		List<Inventory> inventoryList=inventoryRepository.findByCompanyId(companyId);
		inventoryList.stream().forEach((x)->{
			InventoryDTO inventoryDTO=modelMapper.map(x, InventoryDTO.class);
			inventoryListDTO.add(inventoryDTO);
		});
		return inventoryListDTO;
	}
	@Override
	public InventoryDTO getInventory(String id) {
		// TODO Auto-generated method stub
		Optional<Inventory> inventory=inventoryRepository.findById(id);
		InventoryDTO inventoryDTO=modelMapper.map(inventory.get(), InventoryDTO.class);
		return inventoryDTO;
	}
	@Override
	public void deleteInventory(String id) {
		// TODO Auto-generated method stub
		inventoryRepository.deleteById(id);
		
	}
	@Override
	public void addInventoryExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		// TODO Auto-generated method stub
		ExtraFieldName extraFieldNameNew=extraFieldNameRepository.findByNameAndCompanyId(extraFieldNameDTO.getName().toLowerCase(),extraFieldNameDTO.getCompanyId());
		if(extraFieldNameNew!=null) {
			throw new Exception("Extra Field Already Present");
		}
		extraFieldNameDTO.setName(extraFieldNameDTO.getName().toLowerCase());
		
		ExtraFieldName extraFieldName=modelMapper.map(extraFieldNameDTO, ExtraFieldName.class);
		extraFieldNameRepository.save(extraFieldName);
		
	}
	@Override
	public List<ExtraFieldNameDTO> getInventoryExtraField(String companyId) {
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
	public void deleteInventoryExtraField(String id) {
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
	public Map<String, Map<String, String>> getExtraFieldList(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldNameList=extraFieldsRepository.findByCompanyId(companyId);
		List<Inventory> assetList=inventoryRepository.findByCompanyId(companyId);
		Map<String, Map<String,String>> fieldNameValueMap=new HashMap<>();
		
		assetList.stream().forEach((inventory)->{
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((field)->{
				
				if(field.getInventoryId()!=null&&field.getInventoryId().endsWith(inventory.getId()) ) {
					m.put(field.getName(), field.getValue());
					
				}
			
				
				
		
			});
			fieldNameValueMap.put(inventory.getId(), m);
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
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByInventoryId(id);
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
	public void deleteExtraFieldByInventory(String workOrderId) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByInventoryId(workOrderId);
		
		
		extraFieldsList.stream().forEach((x)->{
			
			extraFieldsRepository.delete(x);
		});
		
	}
	@Override
	public List<String> inventoryAllData(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFieldName> extraFieldNameList=extraFieldNameRepository.findByCompanyId(companyId);
//		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(workOrderId);
		List<Inventory> workOrderList= inventoryRepository.findByCompanyId(companyId);
//		WorkOrder workOrder=workOrderOptional.get();
//		WorkOrderWithExtraFieldsDTO workOrderWithExtraFieldsDTO = modelMapper.map(workOrder, WorkOrderWithExtraFieldsDTO.class);
		
		
		List<String> mapList=new ArrayList<>();
		workOrderList.stream().forEach((order)->{
			List<ExtraFields> extraFieldsList=extraFieldsRepository.findByInventoryId(order.getId());
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((x)->{
				m.put(x.getName(), "");
				extraFieldsList.stream().forEach((x1)->{
					m.put(x1.getName(), x1.getValue());
				});
			});
			m.put("id", order.getId());
			m.put("partId",order.getPartId());
			m.put("partImage",order.getPartImage());
			m.put("partName",order.getPartName());
			m.put("companyId",order.getCompanyId());
			if(order.getCost()!=null) m.put("cost",order.getCost().toString());
			m.put("category",order.getCategory());
			if(order.getQuantity()!=null) m.put("quantity",order.getQuantity().toString());
			if(order.getPrice()!=null) m.put("price",order.getPrice().toString());
			
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
	public void updateInventory(InventoryDTO inventoryDTO) {
		// TODO Auto-generated method stub
//		Optional<Inventory> inventoryOptional=inventoryRepository.findById(inventoryDTO.getId());
		Inventory inventory= modelMapper.map(inventoryDTO, Inventory.class);
	
		inventoryRepository.save(inventory);
		
	}

}
