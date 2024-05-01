package com.quantummaintenance.companyCustomer.controller;


import java.io.IOException;
import java.util.List;
import java.util.Map;

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


import com.quantummaintenance.companyCustomer.dto.*;
import com.quantummaintenance.companyCustomer.entity.MandatoryFields;
import com.quantummaintenance.companyCustomer.entity.ShowFields;
import com.quantummaintenance.companyCustomer.service.CompanyCustomerService;





@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping(value="/companycustomer")
public class CompanyCustomerAPI {

	@Autowired
	private CompanyCustomerService companyCustomerService;
	
	@DeleteMapping("/deleteCompanyCustomer/{id}")
	public void deleteCompanyCustomer(@PathVariable String id)  {
		companyCustomerService.deleteCustomer(id);
	}
	@GetMapping("/allCompanyCustomer/{companyId}")
	public List<CompanyCustomerDTO> getCompanyCustomerList(@PathVariable String companyId){
		return companyCustomerService.getAllCustomer(companyId);
	}
	@GetMapping("/getCompanyCustomer/{id}")
	public CompanyCustomerDTO getCompanyCustomer(@PathVariable String id){
		System.out.println(id);
		return companyCustomerService.getCustomer(id);
	}
	@GetMapping("/getCompanyCustomerByLocalId/{id}")
	public CompanyCustomerDTO getCompanyCustomerByLocalId(@PathVariable String id){
		System.out.println(id);
		return companyCustomerService.getCompanyCustomerByLocalId(Integer.valueOf(id));
	}
//	@GetMapping("/allCompanyCustomerWithExtraFields/{companyId}")
//	public List<CompanyCustomerDTO> getCompanyCustomerWithExtraFields(@PathVariable String companyId){
//		return companyCustomerService.getAllCustomer(companyId);
//	}
	@PostMapping("/addCompanyCustomer")
	public void addNewFields(@RequestBody CompanyCustomerDTO companyCustomerDTO){
		companyCustomerService.addCustomer(companyCustomerDTO);
	}
	@PutMapping("/updateCompanyCustomer")
	public void updateCompanyCustomer(@RequestBody CompanyCustomerDTO companyCustomerDTO)  {
		companyCustomerService.updateCustomer(companyCustomerDTO);
	}
	@GetMapping(value="/searchCompanyCustomerlist/{companyId}")
	public List<String> getCompanyCustomerFromAsset(@PathVariable String companyId,@RequestParam(name = "data", required = true) String search,
            @RequestParam(name = "category", required = true) String category ){
		System.out.println("----------my CompanyCustomer search------------->"+search);
				return companyCustomerService.searchedCompanyCustomer(companyId,search, category);
		
	}
	@GetMapping(value="/sortCompanyCustomerlist/{companyId}")
	public List<String> getCompanyCustomerFromAsset(@PathVariable String companyId,
            @RequestParam(name = "category", required = true) String category ){
				return companyCustomerService.sortCompanyCustomer(companyId, category);
		
	}
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		companyCustomerService.addCompanyCustomerExtraField(extraFieldNameDTO);
	}
	@GetMapping("/getExtraFieldName/{companyId}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String companyId){
		System.out.println("----------my company------------->"+companyId);
		return companyCustomerService.getCompanyCustomerExtraField(companyId);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		companyCustomerService.deleteCompanyCustomerExtraField(id);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		companyCustomerService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		companyCustomerService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{companyId}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String companyId){
		System.out.println("============================>"+name+companyId);
		MandatoryFields mandatoryFields=companyCustomerService.getMandatoryFields(name,companyId);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{companyId}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String companyId){
		ShowFields showFields=companyCustomerService.getShowFields(name,companyId);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{companyId}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String companyId){
		List<MandatoryFields> mandatoryFieldsList=companyCustomerService.getAllMandatoryFields(companyId);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{companyId}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String companyId){
		List<ShowFields> showFieldsList=companyCustomerService.getAllShowFields(companyId);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{companyId}")
	public void showFields(@PathVariable String name,@PathVariable String companyId){
		companyCustomerService.deleteShowAndMandatoryFields(companyId, name);
	}
	@GetMapping("/getExtraFieldNameValue/{companyId}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String companyId){
		return companyCustomerService.getextraFieldList(companyId);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		companyCustomerService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return companyCustomerService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		companyCustomerService.deleteExtraFields(id);
	}
	@DeleteMapping("/deleteCompanyCustomerExtraFields/{id}")
	public void deleteCompanyCustomerExtraFields(@PathVariable String id) throws Exception {
		companyCustomerService.deleteExtraFieldByCompanyCustomer(id);
	}
	@GetMapping("/allCompanyCustomerWithExtraFields/{id}")
	public List<String> allCompanyCustomerWithExtraFields(@PathVariable String id){
		return companyCustomerService.getAllCustomerWithExtraColumns(id);
	}
	@PostMapping("/addFile/{companyCustomerId}")
	public ResponseEntity<ResponseMessageDTO> addCompanyCustomerFile(@RequestParam("file") MultipartFile file,@PathVariable String companyCustomerId) {
		System.out.println("------------------------inside Multifile------------->");
		String message = "";
		try {
			companyCustomerService.addCompanyCustomerFile(file,companyCustomerId);
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
	@GetMapping("/getFile/{companyCustomerId}")
	public List<CompanyCustomerFileDTO> getCompanyCustomerFile(@PathVariable String companyCustomerId) {
		return companyCustomerService.getCompanyCustomerFile(companyCustomerId);
	}
	@GetMapping("/getFile/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable String id) {
		CompanyCustomerFileDTO companyCustomerFileDTO=companyCustomerService.downloadFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("json/object")).body(companyCustomerFileDTO.getFile());
	}
	@DeleteMapping("deleteFile/{id}")
	public void deleteFile(@PathVariable String id) {
		companyCustomerService.deleteFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
//		return new ResponseEntity<>("Successfully Deleted File",HttpStatus.EXPECTATION_FAILED);
	}
}
