package com.quantummaintenance.companyCustomer.service;


import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantummaintenance.companyCustomer.dto.CompanyCustomerDTO;
import com.quantummaintenance.companyCustomer.dto.CompanyCustomerFileDTO;
import com.quantummaintenance.companyCustomer.dto.ExtraFieldNameDTO;
import com.quantummaintenance.companyCustomer.dto.ExtraFieldsDTO;
import com.quantummaintenance.companyCustomer.entity.*;
import com.quantummaintenance.companyCustomer.entity.CompanyCustomerFile;
import com.quantummaintenance.companyCustomer.entity.ExtraFieldName;
import com.quantummaintenance.companyCustomer.entity.ExtraFields;
import com.quantummaintenance.companyCustomer.entity.MandatoryFields;
import com.quantummaintenance.companyCustomer.entity.ShowFields;
import com.quantummaintenance.companyCustomer.exception.ExtraFieldAlreadyPresentException;
import com.quantummaintenance.companyCustomer.repository.CompanyCustomerFileRepository;
import com.quantummaintenance.companyCustomer.repository.CompanyCustomerRepository;
import com.quantummaintenance.companyCustomer.repository.ExtraFieldNameRepository;
import com.quantummaintenance.companyCustomer.repository.ExtraFieldsRepository;
import com.quantummaintenance.companyCustomer.repository.IdTableRepository;
import com.quantummaintenance.companyCustomer.repository.MandatoryFieldsRepository;
import com.quantummaintenance.companyCustomer.repository.ShowFieldsRepository;





@Service
public class CompanyCustomerServiceImpl implements CompanyCustomerService {
	
	@Autowired CompanyCustomerRepository companyCustomerRepository;
	
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
	
	@Autowired CompanyCustomerFileRepository companyCustomerFileRepository;
	
	private ModelMapper modelMapper=new ModelMapper();

	@Override
	public CompanyCustomerDTO addCustomer(CompanyCustomerDTO myCustomerDTO) {
		// TODO Auto-generated method stub
		
		CompanyCustomer companyCustomer=modelMapper.map(myCustomerDTO, CompanyCustomer.class);
		if(companyCustomer.getCompanyCustomerId()==null) {
			Optional<IdTable> optionalIdTable=idTableRepository.findByCompanyId(myCustomerDTO.getCompanyId());
			if(optionalIdTable.isEmpty()) {
			companyCustomer.setCompanyCustomerId(1);
			IdTable myidTable=new IdTable();
			myidTable.setTableId(2);
			myidTable.setCompanyId(myCustomerDTO.getCompanyId());
			idTableRepository.save(myidTable);
			
			}
			else {
			
				IdTable idTable=optionalIdTable.get();
				companyCustomer.setCompanyCustomerId(idTable.getTableId());
				idTable.updateId();
				idTableRepository.save(idTable);
			}
		}
		CompanyCustomerDTO myCompanyCustomerDTO=modelMapper.map(companyCustomerRepository.save(companyCustomer), CompanyCustomerDTO.class);
		return myCompanyCustomerDTO;
		
		
	}

	@Override
	public CompanyCustomerDTO getCustomer(String id) {
		// TODO Auto-generated method stub
		Optional<CompanyCustomer> companyCustomerOptional=companyCustomerRepository.findById(id);
		System.out.println(id);
		CompanyCustomerDTO companyCustomerDTO= modelMapper.map(companyCustomerOptional.get(), CompanyCustomerDTO.class);
		return companyCustomerDTO;
	}

	@Override
	public List<CompanyCustomerDTO> getAllCustomer(String companyId) {
		// TODO Auto-generated method stub
		List<CompanyCustomer> companyCustomerList=companyCustomerRepository.findByCompanyId(companyId);
		System.out.println("-----------------------my list---------------->"+companyCustomerList.size());
		List<CompanyCustomerDTO> companyCustomerDTOList=new ArrayList<>();
		companyCustomerList.stream().forEach((x)->{
			CompanyCustomerDTO companyCustomerDTO=modelMapper.map(x, CompanyCustomerDTO.class);
			System.out.println(companyCustomerDTO.getCompanyCustomerId());
			companyCustomerDTO.getCompanyCustomerId();
			companyCustomerDTOList.add(companyCustomerDTO);
		});
		return companyCustomerDTOList;
	}

	@Override
	public void updateCustomer(CompanyCustomerDTO companyCustomerDTO) {
		// TODO Auto-generated method stub
		Optional<CompanyCustomer> companyCustomerOptional=companyCustomerRepository.findById(companyCustomerDTO.getId());
		CompanyCustomer companyCustomer= modelMapper.map(companyCustomerDTO, CompanyCustomer.class);
		
		companyCustomerRepository.save(companyCustomer);
	}

	@Override
	public void deleteCustomer(String id) {
		// TODO Auto-generated method stub
		Optional<CompanyCustomer> companyCustomerOptional=companyCustomerRepository.findById(id);
		companyCustomerRepository.delete(companyCustomerOptional.get());
	}

	@Override
	public List<String> getAllCustomerWithExtraColumns(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFieldName> extraFieldNameList=extraFieldNameRepository.findByCompanyId(companyId);
//		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(workOrderId);
		List<CompanyCustomer> workOrderList= companyCustomerRepository.findByCompanyId(companyId);
//		WorkOrder workOrder=workOrderOptional.get();
//		WorkOrderWithExtraFieldsDTO workOrderWithExtraFieldsDTO = modelMapper.map(workOrder, WorkOrderWithExtraFieldsDTO.class);
		
		
		List<String> mapList=new ArrayList<>();
		workOrderList.stream().forEach((order)->{
			List<ExtraFields> extraFieldsList=extraFieldsRepository.findByCompanyCustomerId(order.getId());
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((x)->{
				m.put(x.getName(), "");
				extraFieldsList.stream().forEach((x1)->{
					m.put(x1.getName(), x1.getValue());
				});
			});
			m.put("id", order.getId());
			m.put("name",order.getName());
			m.put("companyId",order.getCompanyId());
			m.put("category",order.getCategory());
			m.put("status",order.getStatus());
			m.put("address",order.getAddress());
			m.put("email",order.getEmail());
			m.put("apartment",order.getApartment());
			m.put("city",order.getCity());
			m.put("state",order.getState());
			m.put("companyCustomerId",order.getCompanyCustomerId().toString());
			if(order.getPhone()!=null) m.put("phone",order.getPhone().toString());
			if(order.getZipCode()!=null) m.put("zipCode",order.getZipCode().toString());
			
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
	public List<String> searchedCompanyCustomer(String companyId, String search, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> sortCompanyCustomer(String companyId, String category) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void addCompanyCustomerExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException {
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
	public List<ExtraFieldNameDTO> getCompanyCustomerExtraField(String companyId) {
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
	public void deleteCompanyCustomerExtraField(String id) {
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
	public ShowFields getShowFields(String name, String companyId) {
		// TODO Auto-generated method stub
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
	public List<MandatoryFields> getAllMandatoryFields(String companyId) {
		// TODO Auto-generated method stub
		List<MandatoryFields> mandatoryFieldsList=mandatoryFieldsRepository.findByCompanyId(companyId);
		return mandatoryFieldsList;
	}

	@Override
	public List<ShowFields> getAllShowFields(String companyId) {
		// TODO Auto-generated method stub
		List<ShowFields> showFieldsList=showFieldsRepository.findByCompanyId(companyId);
		return showFieldsList;
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
		List<CompanyCustomer> assetList=companyCustomerRepository.findByCompanyId(companyId);
		Map<String, Map<String,String>> fieldNameValueMap=new HashMap<>();
		
		assetList.stream().forEach((workorder)->{
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((field)->{
				
				if(field.getCompanyCustomerId().endsWith(workorder.getId()) ) {
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
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByCompanyCustomerId(id);
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
	public void deleteExtraFieldByCompanyCustomer(String id) {
		// TODO Auto-generated method stub
List<ExtraFields> extraFieldsList=extraFieldsRepository.findByCompanyCustomerId(id);
		
		
		extraFieldsList.stream().forEach((x)->{
			
			extraFieldsRepository.delete(x);
		});
		
	}

	@Override
	public CompanyCustomerFile addCompanyCustomerFile(MultipartFile file, String companyCustomerId) throws IOException {
		// TODO Auto-generated method stub
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		 CompanyCustomerFile assetFile = new CompanyCustomerFile();
		    assetFile.setAssetId(companyCustomerId);
		    assetFile.setFile(file.getBytes());
		    assetFile.setFileName(fileName);

		    return companyCustomerFileRepository.save(assetFile);
	}

	@Override
	public List<CompanyCustomerFileDTO> getCompanyCustomerFile(String companyCustomerId) {
		// TODO Auto-generated method stub
		List<CompanyCustomerFile> companyCustomerList=companyCustomerFileRepository.findByAssetId(companyCustomerId);
		if(companyCustomerList.size()==0) {
			return null;
		}
		else {
			List<CompanyCustomerFileDTO> companyCustomerListDTOList=new ArrayList<>();
			companyCustomerList.stream().forEach((x)->{
				CompanyCustomerFileDTO assetFileDTO=modelMapper.map(x, CompanyCustomerFileDTO.class);
				
				companyCustomerListDTOList.add(assetFileDTO);
			});
			
			
			return companyCustomerListDTOList;
		}
	}

	@Override
	public CompanyCustomerFileDTO downloadFile(String id) {
		// TODO Auto-generated method stub
		Optional<CompanyCustomerFile> companyCustomerFile=companyCustomerFileRepository.findById(id);
		CompanyCustomerFileDTO companyCustomerFileDTO=modelMapper.map(companyCustomerFile.get(), CompanyCustomerFileDTO.class);
		return companyCustomerFileDTO;
	}

	@Override
	public void deleteFile(String id) {
		// TODO Auto-generated method stub
		companyCustomerFileRepository.deleteById(id);
	}

	@Override
	public CompanyCustomerDTO getCompanyCustomerByLocalId(Integer id) {
		// TODO Auto-generated method stub
		Optional<CompanyCustomer> companyCustomerOptional=companyCustomerRepository.findByCompanyCustomerId(id);
		if(companyCustomerOptional.isEmpty()) {
			return null;
		}
		System.out.println(id);
		CompanyCustomerDTO companyCustomerDTO= modelMapper.map(companyCustomerOptional.get(), CompanyCustomerDTO.class);
		return companyCustomerDTO;
	}

}
