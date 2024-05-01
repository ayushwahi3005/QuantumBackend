package com.quantummaintenance.assests.service;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Optional;
import java.io.IOException;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.quantummaintenance.assests.dto.AssetFileDTO;
import com.quantummaintenance.assests.dto.AssetImageDTO;
import com.quantummaintenance.assests.dto.AssetsDTO;
import com.quantummaintenance.assests.dto.CheckInDTO;
import com.quantummaintenance.assests.dto.CheckInOutDTO;
import com.quantummaintenance.assests.dto.ExtraFieldNameDTO;
import com.quantummaintenance.assests.dto.ExtraFieldsDTO;
import com.quantummaintenance.assests.entity.AssetFile;
import com.quantummaintenance.assests.entity.Assets;
import com.quantummaintenance.assests.entity.CheckInOut;
import com.quantummaintenance.assests.entity.CheckInOutDetails;
import com.quantummaintenance.assests.entity.ExtraFieldName;
import com.quantummaintenance.assests.entity.ExtraFields;
import com.quantummaintenance.assests.entity.MandatoryFields;
import com.quantummaintenance.assests.entity.ShowFields;
import com.quantummaintenance.assests.entity.IdTable;
import com.quantummaintenance.assests.exception.ExtraFieldAlreadyPresentException;
import com.quantummaintenance.assests.repository.AssetFileRepository;
import com.quantummaintenance.assests.repository.AssetsRepository;
import com.quantummaintenance.assests.repository.CheckInOutRepository;
import com.quantummaintenance.assests.repository.ExtraFieldNameRepository;
import com.quantummaintenance.assests.repository.ExtraFieldsRepository;
import com.quantummaintenance.assests.repository.IdTableRepository;
import com.quantummaintenance.assests.repository.MandatoryFieldsRepository;
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

	

	

}
