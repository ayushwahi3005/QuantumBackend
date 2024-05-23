package com.quantummaintenance.assests.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.util.stream.Collectors;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Set;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quantummaintenance.assests.dto.*;

import com.quantummaintenance.assests.entity.AssetFile;
import com.quantummaintenance.assests.entity.Assets;
import com.quantummaintenance.assests.entity.CheckInOut;
import com.quantummaintenance.assests.entity.CheckInOutDetails;
import com.quantummaintenance.assests.entity.ExtraFieldName;
import com.quantummaintenance.assests.entity.ExtraFields;
import com.quantummaintenance.assests.entity.MandatoryFields;
import com.quantummaintenance.assests.entity.QR;
import com.quantummaintenance.assests.entity.ShowFields;
import com.quantummaintenance.assests.entity.StatusEnum;
import com.quantummaintenance.assests.entity.IdTable;
import com.quantummaintenance.assests.exception.ExtraFieldAlreadyPresentException;
import com.quantummaintenance.assests.repository.AssetFileRepository;
import com.quantummaintenance.assests.repository.AssetsRepository;
import com.quantummaintenance.assests.repository.CheckInOutRepository;
import com.quantummaintenance.assests.repository.ExtraFieldNameRepository;
import com.quantummaintenance.assests.repository.ExtraFieldsRepository;
import com.quantummaintenance.assests.repository.IdTableRepository;
import com.quantummaintenance.assests.repository.MandatoryFieldsRepository;
import com.quantummaintenance.assests.repository.QRRepository;
import com.quantummaintenance.assests.repository.ShowFieldsRepository;

@Service
public class AssetsServiceImpl implements AssetsService {

	@Autowired
	private AssetFileRepository assetFileRepository;
	
	@Autowired
	private AssetsRepository assetsRepository;
	@Autowired
	private ExtraFieldsRepository extraFieldsRepository;
	
	@Autowired
	private ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	private CheckInOutRepository checkInOutRepository;
	
	@Autowired
	private MandatoryFieldsRepository mandatoryFieldsRepository;
	@Autowired
	private ShowFieldsRepository showFieldsRepository;
	
	@Autowired
	private IdTableRepository idTableRepository;
	
	@Autowired
	private QRRepository qrRepository;
	
	 private ModelMapper modelMapper=new ModelMapper();
	@Override
	public List<AssetsDTO> getAssetsDetails(String companyId) {
		// TODO Auto-generated method stub
		List<Assets> assetsList=assetsRepository.findByCompanyId(companyId);
		List<AssetsDTO> assetsDTOList=new ArrayList<AssetsDTO>();
		assetsList.stream().forEach(x->{
			AssetsDTO assetsDTO=modelMapper.map(x, AssetsDTO.class);
			assetsDTOList.add(assetsDTO);
		});
		return assetsDTOList;
	}
	@Override
	public AssetsDTO addAssets(AssetsDTO assetsDTO) {
		// TODO Auto-generated method stub
		Assets assets=modelMapper.map(assetsDTO, Assets.class);
		if(assets.getAssetId()==null) {
			Optional<IdTable> optionalIdTable=idTableRepository.findByCompanyId(assetsDTO.getCompanyId());
			if(optionalIdTable.isEmpty()) {
				assets.setAssetId(1);
				IdTable myidTable=new IdTable();
				myidTable.setTableId(2);
				myidTable.setCompanyId(assetsDTO.getCompanyId());
//				System.out.println("---------------------new---------->"+idTable.getTableId());
				idTableRepository.save(myidTable);
			}
			else {
//				List<IdTable> idTableList=idTableRepository.findAll();
//				IdTable idTable=idTableList.get(0);
				IdTable idTable=optionalIdTable.get();
				assets.setAssetId(idTable.getTableId());
				idTable.updateId();
//				System.out.println("---------------------already---------->"+idTable.getTableId()+" "+idTable.get);
				idTableRepository.save(idTable);
			}
		}

		
		AssetsDTO myAssetsDTO=modelMapper.map(assetsRepository.save(assets),AssetsDTO.class);
		return myAssetsDTO;
		
	}
	@Override
	public void importExcel(List<AssetsDTO> assetsDTOList,Map<String,String> columnMap) {
		// TODO Auto-generated method stub
		
		if(assetsDTOList.isEmpty()) {
			return;
		}
		Optional<IdTable> optionalIdTable=idTableRepository.findByCompanyId(assetsDTOList.get(0).getCompanyId());
		assetsDTOList.stream().forEach(x->{
			
			Assets assets=modelMapper.map(x, Assets.class);
			if(optionalIdTable.isEmpty()) {
				assets.setAssetId(1);
				IdTable myidTable=new IdTable();
				myidTable.setTableId(2);
				myidTable.setCompanyId(assetsDTOList.get(0).getCompanyId());
//				System.out.println("---------------------new---------->"+idTable.getTableId());
				idTableRepository.save(myidTable);
			}
			else {
			
			IdTable myidTable=optionalIdTable.get();
			assets.setAssetId(myidTable.getTableId());
			myidTable.updateId();
			idTableRepository.save(myidTable);
			
			}
			assetsRepository.save(assets);
		});
		
		
	}
	@Override
	public void addImage(AssetImageDTO assetImageDTO) throws Exception {
		// TODO Auto-generated method stub
		Optional<Assets> optionalAssets=assetsRepository.findById(assetImageDTO.getId());
		Assets asset=optionalAssets.orElseThrow(()-> new Exception("No Such Asset"));
		asset.setImage(assetImageDTO.getImage());
		
		assetsRepository.save(asset);
		
	}
	@Override
	public void removeImage(String id) throws Exception {
		// TODO Auto-generated method stub
		Optional<Assets> optionalAssets=assetsRepository.findById(id);
		Assets asset=optionalAssets.orElseThrow(()-> new Exception("No Such Asset"));
		asset.setImage("");
		assetsRepository.save(asset);
		
	}
	@Override
	public void removeAsset(String id) throws Exception {
		// TODO Auto-generated method stub
		
		assetsRepository.deleteById(id);
		
	}
	@Override
	public AssetsDTO getAsset(String assetId) throws Exception {
		// TODO Auto-generated method stub
		Optional<Assets> optionalasset=assetsRepository.findById(assetId);
		Assets asset=optionalasset.orElseThrow(()-> new Exception("No Such Asset"));
		AssetsDTO assetDTO=modelMapper.map(asset, AssetsDTO.class);
		return assetDTO;
		
	}
	@Override
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO) throws Exception {
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
		List<ExtraFields> extraFieldsList=extraFieldsRepository.findByAssetId(id);
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
	public Map<String, Map<String,String>> getextraFieldList(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFields> extraFieldNameList=extraFieldsRepository.findByCompanyId(companyId);
		List<Assets> assetList=assetsRepository.findByCompanyId(companyId);
		Map<String, Map<String,String>> fieldNameValueMap=new HashMap<>();
		
		assetList.stream().forEach((asset)->{
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((field)->{
				
				if(field.getAssetId().endsWith(asset.getId()) ) {
					m.put(field.getName(), field.getValue());
					
				}
			
				
				
		
			});
			fieldNameValueMap.put(asset.getId(), m);
		});
		return fieldNameValueMap;
	}
	@Override
	public void addCheckInOut(CheckInDTO checkInDTO) {
		// TODO Auto-generated method stub
		
//		checkInOutDetailsDTO.stream().forEach((x)->{
//			System.out.println("--------------------------------------------"+x.getDate());
//		});
//		System.out.println("--------------------------------------------"+checkInOutDTO.getDetailsList().size());
		List<CheckInOut> checkInOutList=checkInOutRepository.findByAssetId(checkInDTO.getAssetId());
	
		
		if(checkInOutList.isEmpty()) {
			CheckInOut checkInOut=new CheckInOut();
			checkInOut.setAssetId(checkInDTO.getAssetId());
			checkInOut.setStatus(checkInDTO.getStatus());
			
			CheckInOutDetails checkInOutDetails=new CheckInOutDetails();
			checkInOutDetails.setStatus(checkInDTO.getStatus());
			checkInOutDetails.setDate(checkInDTO.getDate());
			checkInOutDetails.setEmployee(checkInDTO.getEmployee());
			checkInOutDetails.setLocation(checkInDTO.getLocation());
			checkInOutDetails.setNotes(checkInDTO.getNotes());
			List<CheckInOutDetails> checkInOutDetailsList=new ArrayList<>();
			checkInOutDetailsList.add(checkInOutDetails);
			checkInOut.setDetailsList(checkInOutDetailsList);
			checkInOutRepository.save(checkInOut);
			
			
			
			
		}
		else {
			checkInOutList.stream().forEach((x)->{
				
				x.setAssetId(checkInDTO.getAssetId());
				x.setStatus(checkInDTO.getStatus());

				
				List<CheckInOutDetails> checkInOutDetailsList=x.getDetailsList();
				CheckInOutDetails checkInOutDetails=new CheckInOutDetails();
				checkInOutDetails.setStatus(checkInDTO.getStatus());
				checkInOutDetails.setDate(checkInDTO.getDate());
				checkInOutDetails.setEmployee(checkInDTO.getEmployee());
				checkInOutDetails.setLocation(checkInDTO.getLocation());
				checkInOutDetails.setNotes(checkInDTO.getNotes());
				checkInOutDetailsList.add(checkInOutDetails);
				x.setDetailsList(checkInOutDetailsList);
				checkInOutRepository.save(x);
			});
		}
		
		
	
		
	}
	@Override
	public List<CheckInOutDTO> getCheckOutInList(String assetId) {
		// TODO Auto-generated method stub
		List<CheckInOut>  checkInOutList=checkInOutRepository.findByAssetId(assetId);
		List<CheckInOutDTO>  checkInOutDTOList=new ArrayList<>();
		if(!checkInOutList.isEmpty()) {
			
			checkInOutList.stream().forEach((x)->{
				CheckInOutDTO checkInOutDTO=modelMapper.map(x, CheckInOutDTO.class);
				checkInOutDTOList.add(checkInOutDTO);
			});
		}
		return checkInOutDTOList;
	}
	@Override
	public AssetFile addAssetFile(MultipartFile file,String assetId) throws IOException {
		// TODO Auto-generated method stub
		 String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		    AssetFile assetFile = new AssetFile();
		    assetFile.setAssetId(assetId);
		    assetFile.setFile(file.getBytes());
		    assetFile.setFileName(fileName);

		    return assetFileRepository.save(assetFile);
//		AssetFile assetFile=modelMapper.map(assetFileDTO, AssetFile.class);
//		assetFileRepository.save(assetFile);
		
		
	}
	@Override
	public List<AssetFileDTO> getAssetFile(String assetId) {
		// TODO Auto-generated method stub
		
		List<AssetFile> assetFileList=assetFileRepository.findByAssetId(assetId);
		if(assetFileList.size()==0) {
			return null;
		}
		else {
			List<AssetFileDTO> assetFileDTOList=new ArrayList<>();
			assetFileList.stream().forEach((x)->{
				AssetFileDTO assetFileDTO=modelMapper.map(x, AssetFileDTO.class);
				
				assetFileDTOList.add(assetFileDTO);
			});
			
			
			return assetFileDTOList;
		}
		
	}
	@Override
	public AssetFileDTO downloadFile(String id) {
		// TODO Auto-generated method stub
		Optional<AssetFile> assetFile=assetFileRepository.findById(id);
		AssetFileDTO assetFileDTO=modelMapper.map(assetFile.get(), AssetFileDTO.class);
		return assetFileDTO;
	}
	@Override
	public void deleteFile(String id) {
		// TODO Auto-generated method stub
		assetFileRepository.deleteById(id);
		
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
		showFieldsRepository.delete(showFieldsOptional.get());
		Optional<MandatoryFields> mandatoryFieldsOptional=mandatoryFieldsRepository.findByNameAndCompanyId(name, companyId);
		if(mandatoryFieldsOptional.isPresent()) {
		mandatoryFieldsRepository.delete(mandatoryFieldsOptional.get());
		}
	}
	@Override
	public void updateAssetWithFile(List<AssetsDTO> assetsDTOList,String companyId) {
		// TODO Auto-generated method stub
		
		
		
		
		
		assetsDTOList.stream().forEach((x)->{
			System.out.println("------------------------->>>"+x.getAssetId());
			Assets assets=assetsRepository.findByAssetIdAndCompanyId(x.getAssetId(),companyId);
			x.setId(assets.getId());
			x.setImage(assets.getImage());
			
			
			Assets myasset=modelMapper.map(x, Assets.class);
			assetsRepository.save(myasset);
		});
		
	}
	@Override
	public void qrDataUpdation(QR qr) {
		// TODO Auto-generated method stub
		Optional<QR> optionalQr = qrRepository.findByCompanyId(qr.getCompanyId());
		if(optionalQr.isEmpty()) {
			qrRepository.save(qr);
		}
		else {
			qr.setId(optionalQr.get().getId());
			qrRepository.save(qr);
		}
		
		
	}
	@Override
	public QR getQRData(String companyId) {
		// TODO Auto-generated method stub
		Optional<QR> optionalQr = qrRepository.findByCompanyId(companyId);
		if(optionalQr.isPresent()) {
			return optionalQr.get();
		}
		return null;
	}
	@Override
	public PaginatedResultDTO<String> getAllAssetDetails(String companyId) {
		// TODO Auto-generated method stub
		List<ExtraFieldName> extraFieldNameList=extraFieldNameRepository.findByCompanyId(companyId);

		List<Assets> assetList= assetsRepository.findByCompanyId(companyId);

		
		
		List<String> mapList=new ArrayList<>();
		assetList.stream().forEach((order)->{
			List<ExtraFields> extraFieldsList=extraFieldsRepository.findByAssetId(order.getId());
			Map<String,String> m=new HashMap<>();
			extraFieldNameList.stream().forEach((x)->{
				m.put(x.getName(), "");
				extraFieldsList.stream().forEach((x1)->{
					m.put(x1.getName(), x1.getValue());
				});
			});
			m.put("id", order.getId());
			m.put("image",order.getImage());
			m.put("email",order.getEmail());
			m.put("name",order.getName());
			m.put("assetId",order.getAssetId().toString());
			m.put("companyId",order.getCompanyId());
			m.put("serialNumber",order.getSerialNumber());
			m.put("category",order.getCategory());
			m.put("customer",order.getCustomer());
			m.put("customerId",order.getCustomerId());
			m.put("location",order.getLocation());
			m.put("status",order.getStatus());
			
			
//			if(order.getDueDate()!=null) m.put("dueDate",order.getDueDate().toString());
//			if(order.getLastUpdate()!=null) m.put("lastUpdate",order.getLastUpdate().toString());
//			if(order.getPriority()!=null) m.put("priority",order.getPriority().toString());
//			if(order.getStatus()!=null) m.put("status",order.getStatus().toString());
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
		

		
		 return new PaginatedResultDTO<>(mapList, mapList.size());
	}
	@Override
	public List<String> sortAssets(String companyId, String field) {
		// TODO Auto-generated method stub
		ExtraFieldName extraFieldName=extraFieldNameRepository.findByNameAndCompanyId(field, companyId);
		System.out.println(extraFieldName);
		// TODO Auto-generated method stub
//		String type= extraFieldName.getType();
		List<Map<String,String> > mapList=new ArrayList<>();
		PaginatedResultDTO<String> myList=getAllAssetDetails(companyId);
		ObjectMapper objectMapper = new ObjectMapper();
		myList.getData().stream().forEach((asset)->{
			Map<String,String> m=new HashMap<>();
			 try {
		            // Convert JSON string to Map<String, String>
		            m= objectMapper.readValue(asset, new TypeReference<Map<String, String>>() {});
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
	}
	@Override
	public List<String> searchedAssets(String companyId, String data, String field) {
		// TODO Auto-generated method stub
		List<Map<String,String> > mapList=new ArrayList<>();
		PaginatedResultDTO<String> myList=getAllAssetDetails(companyId);
		ObjectMapper objectMapper = new ObjectMapper();
		myList.getData().stream().forEach((asset)->{
			Map<String,String> m=new HashMap<>();
			 try {
		            // Convert JSON string to Map<String, String>
		            m= objectMapper.readValue(asset, new TypeReference<Map<String, String>>() {});
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
	}
	@Override
	public List<AssetsDTO> getAssetsDetailsByCustomerId(String customerId) {
		// TODO Auto-generated method stub
		List<Assets> assetsList=assetsRepository.findByCustomerId(customerId);
		List<AssetsDTO> assetsDTOList=new ArrayList<AssetsDTO>();
		assetsList.stream().forEach(x->{
			AssetsDTO assetsDTO=modelMapper.map(x, AssetsDTO.class);
			assetsDTOList.add(assetsDTO);
			
		});
		System.out.println("Asset by cutomer"+assetsDTOList.size());
		return assetsDTOList;
	}
	@Override
	public void updateAssetsWithInActive(String customerId) {
		// TODO Auto-generated method stub
		List<Assets> assetsList=assetsRepository.findByCustomerId(customerId);
		
		assetsList.stream().forEach(x->{
			x.setStatus("inActive");
			assetsRepository.save(x);
			
		});
		
		
	}
	@Override
	public PaginatedResultDTO<String> advanceFilter(Object filter, int pageNumber, int pageSize) {
		// TODO Auto-generated method stub
		 List<String> filteredAssetsWithAllFields=new ArrayList<>();
		 long totalPage=0;
		System.out.println("----filter--->"+filter);
		 if (filter instanceof Map) {
	            // Cast the filter to a Map
	            Map<?, ?> filterMap = (Map<?, ?>) filter;
	            
	            // Get all keys
	            Set<?> keys = filterMap.keySet();
	            Map<String,String> mapping=new HashMap<String,String>();
	            
	            // Print keys or do something with them
	            
	            for (Map.Entry<?, ?> entry : filterMap.entrySet()) {
	                Object key = entry.getKey();
	                Object value = entry.getValue();
//	                System.out.println("Key: " + key + ", Value: " + value);
	                if(value!=null) { mapping.put(key.toString(), value.toString());}
	               
	            }
	            PaginatedResultDTO<String> assetsWithAllFields=getAllAssetDetails(mapping.get("companyId"));
	           
//	            assetsWithAllFields.stream().forEach(data->{
//	            	ObjectMapper mapper = new ObjectMapper();
//	            	int flag=1;
//	            	try {
//						Map<String,String> map = mapper.readValue(data, Map.class);
//						for (Map.Entry<?, ?> entry : filterMap.entrySet()) {
//			                Object key = entry.getKey();
//			                Object value = entry.getValue();
////			                System.out.println("Key: " + key + ", Value: " + value);
//			                if(value!=null) { 
//				                mapping.put(key.toString(), value.toString());
//				                String myValue=map.get(key);
//				                String expectedValue=value.toString();
//				                String keyString=key.toString();
//				                if((keyString.equals("companyId")==false)&& myValue!=null&&value!=null&&value.toString()!="") {
//				                	myValue=myValue.toLowerCase();
//				                	 System.out.println("Key: " + key + ", Value: " + value);
//				                	 if(myValue.contains(expectedValue.toLowerCase())==false&&myValue.equals(expectedValue.toLowerCase())==false) flag=0;
//				                	
//				                }
//			                }
//			               
//			               
//			            }
//						
//						if(flag==1) {
//							filteredAssetsWithAllFields.add(data);
//						}
//						
//					} catch (JsonMappingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					} catch (JsonProcessingException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//	            	System.out.println("----------");
//                });
	            
	            
	            filteredAssetsWithAllFields = assetsWithAllFields.getData().stream().filter(data -> {
	                ObjectMapper mapper = new ObjectMapper();
	                int flag = 1;
	                try {
	                    Map<String, String> map = mapper.readValue(data, Map.class);
	                    for (Map.Entry<?, ?> entry : filterMap.entrySet()) {
	                        Object key = entry.getKey();
	                        Object value = entry.getValue();
	                        if (value != null) {
	                            mapping.put(key.toString(), value.toString());
	                            String myValue = map.get(key);
	                            String expectedValue = value.toString();
	                            String keyString = key.toString();
	                            if (!keyString.equals("companyId") && myValue != null && value != null && !value.toString().isEmpty()) {
	                                myValue = myValue.toLowerCase();
	                                if (!myValue.contains(expectedValue.toLowerCase()) && !myValue.equals(expectedValue.toLowerCase())) {
	                                    flag = 0;
	                                }
	                            }
	                        }
	                    }
	                    if (flag == 1) {
	                        return true;
	                    }
	                } catch (JsonMappingException e) {
	                    e.printStackTrace();
	                } catch (JsonProcessingException e) {
	                    e.printStackTrace();
	                }
	                return false;
	            }).collect(Collectors.toList());
	            
	            int startItem = pageNumber * pageSize;
	            int endItem = Math.min(startItem + pageSize, filteredAssetsWithAllFields.size());
	            if (startItem > filteredAssetsWithAllFields.size()) {

	                return new PaginatedResultDTO<>( Collections.emptyList(), 0);
	            }
	            totalPage=filteredAssetsWithAllFields.size();
	            filteredAssetsWithAllFields = filteredAssetsWithAllFields.subList(startItem, endItem);

	           
	        } else {
	            System.out.println("The filter is not a Map instance");
	        }
		 
//		System.out.println(filteredAssetsWithAllFields.size());
		System.out.println("----------"+filteredAssetsWithAllFields.size());
		 return new PaginatedResultDTO<>(filteredAssetsWithAllFields, totalPage);

		
	}

	

	

	

}
