package com.quantummaintenance.assests.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.quantummaintenance.assests.dto.AssetFileDTO;
import com.quantummaintenance.assests.dto.AssetImageDTO;
import com.quantummaintenance.assests.dto.AssetsDTO;
import com.quantummaintenance.assests.dto.CheckInDTO;
import com.quantummaintenance.assests.dto.CheckInOutDTO;
import com.quantummaintenance.assests.dto.ExtraFieldNameDTO;
import com.quantummaintenance.assests.dto.ExtraFieldsDTO;
import com.quantummaintenance.assests.dto.ResponseMessageDTO;
import com.quantummaintenance.assests.entity.MandatoryFields;
import com.quantummaintenance.assests.entity.ShowFields;
import com.quantummaintenance.assests.service.AssetsService;
//@CrossOrigin("http://localhost:4200/")
//@CrossOrigin("*")
@RestController
@RequestMapping("/assets")
public class AssetsAPI {

	
	@Autowired
	private AssetsService assetsService;
	
	RestTemplate restTemplate = new RestTemplate();
	
	@GetMapping("/{email}")
	public List<AssetsDTO> getAssets(@PathVariable String email){
		return assetsService.getAssetsDetails(email);
	}
	@PutMapping("/addassets")
	public void addAssets(@RequestBody AssetsDTO assestsDTO) {
		assetsService.addAssets(assestsDTO);
	}
	@PostMapping("/import/{email}")
	public void importFile(@RequestParam("file") MultipartFile excel,@PathVariable String email) {
		
		List<AssetsDTO> assetList=new ArrayList<AssetsDTO>();           
		try {
			XSSFWorkbook workbook = new XSSFWorkbook(excel.getInputStream());
			XSSFSheet sheet = workbook.getSheetAt(0);
			
			for(int i=0; i<sheet.getPhysicalNumberOfRows();i++) {
				XSSFRow row = sheet.getRow(i);
				AssetsDTO assestsDTO=new AssetsDTO();
				for(int j=0;j<=row.getPhysicalNumberOfCells();j++) {
				
					System.out.print("-------------->"+j+" "+row.getCell(j) +" ");
					if(j==0) {
						assestsDTO.setEmail(email);
					}
					if(j==1) {
						assestsDTO.setName(row.getCell(j-1).toString());
					}
					if(j==2) {
						
						assestsDTO.setSerialNumber(row.getCell(j-1).toString().substring(0,row.getCell(j-1).toString().length()-2));
					}
					if(j==3) {
						assestsDTO.setCategory(row.getCell(j-1).toString());
					}
					if(j==4) {
						assestsDTO.setCustomer(row.getCell(j-1).toString());
					}
					if(j==5) {
						assestsDTO.setLocation(row.getCell(j-1).toString());
					}
					if(j==6) {
						assestsDTO.setStatus(row.getCell(j-1).toString());
					}
					
				}
				assetList.add(assestsDTO);
				System.out.println("");
			}
			System.out.println("-------------------------->inside");
			assetsService.importExcel(assetList);
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
	}
	@PostMapping("/imageUpload")
	public void importFile(@RequestBody AssetImageDTO assetImageDTO) throws Exception {
		
		assetsService.addImage(assetImageDTO);
		
		
	}
	@PostMapping("/removeImage")
	public void removeImage(@RequestBody String id) throws Exception {
		
		
		assetsService.removeImage(id);
		
		
	}
	@PostMapping("/removeAsset")
	public void removeAsset(@RequestBody String id) throws Exception {
		
		
		assetsService.removeAsset(id);
		
		
	}
	@GetMapping("/getAsset/{id}")
	public AssetsDTO getAsset(@PathVariable String id) throws Exception {
		return assetsService.getAsset(id);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		assetsService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return assetsService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		assetsService.deleteExtraFields(id);
	}
	@GetMapping("/getExtraFieldName/{email}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String email){
		return assetsService.getAssetExtraField(email);
	}
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		assetsService.addAssetExtraField(extraFieldNameDTO);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		assetsService.deleteAssetExtraField(id);
	}
	@GetMapping("/getExtraFieldNameValue/{email}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String email){
		return assetsService.getextraFieldList(email);
	}
	@PostMapping("/addCheckInOut")
	public void addCheckInOut(@RequestBody CheckInDTO checkInDTO){
		assetsService.addCheckInOut(checkInDTO);
	}
		
	@GetMapping("/getCheckInOutList/{assetId}")
	public ResponseEntity<List<CheckInOutDTO>> getCheckInOutList(@PathVariable String assetId){
		List<CheckInOutDTO> checkInOutList=assetsService.getCheckOutInList(assetId);
		return new ResponseEntity<>(checkInOutList,HttpStatus.ACCEPTED);
	}
	@PostMapping("/addFile/{assetId}")
	public ResponseEntity<ResponseMessageDTO> addAssetFile(@RequestParam("file") MultipartFile file,@PathVariable String assetId) {
		String message = "";
		try {
			assetsService.addAssetFile(file,assetId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			ResponseMessageDTO responseMessageDTO=new ResponseMessageDTO();
			responseMessageDTO.setResponseMessage(message);
		      return new ResponseEntity<>(responseMessageDTO,HttpStatus.ACCEPTED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			ResponseMessageDTO responseMessageDTO=new ResponseMessageDTO();
			responseMessageDTO.setResponseMessage(message);
		      return new ResponseEntity<>(responseMessageDTO,HttpStatus.EXPECTATION_FAILED);
		}
	}
	@GetMapping("/getFile/{assetId}")
	public List<AssetFileDTO> getAssetFile(@PathVariable String assetId) {
		return assetsService.getAssetFile(assetId);
	}
	@GetMapping("/getFile/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable String id) {
		AssetFileDTO assetFileDTO=assetsService.downloadFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("json/object")).body(assetFileDTO.getFile());
	}
	@DeleteMapping("deleteFile/{id}")
	public void deleteFile(@PathVariable String id) {
		assetsService.deleteFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
//		return new ResponseEntity<>("Successfully Deleted File",HttpStatus.EXPECTATION_FAILED);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		assetsService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		assetsService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{email}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String email){
		System.out.println("============================>"+name+email);
		MandatoryFields mandatoryFields=assetsService.getMandatoryFields(name,email);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{email}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String email){
		ShowFields showFields=assetsService.getShowFields(name,email);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{email}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String email){
		List<MandatoryFields> mandatoryFieldsList=assetsService.getAllMandatoryFields(email);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{email}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String email){
		List<ShowFields> showFieldsList=assetsService.getAllShowFields(email);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{email}")
	public void showFields(@PathVariable String name,@PathVariable String email){
		assetsService.deleteShowAndMandatoryFields(email, name);
	}
	
}
