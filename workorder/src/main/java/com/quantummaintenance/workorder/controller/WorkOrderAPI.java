package com.quantummaintenance.workorder.controller;


import java.util.Date;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.springframework.http.*;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


import com.quantummaintenance.workorder.dto.CustomerDTO;
import com.quantummaintenance.workorder.dto.ExtraFieldNameDTO;
import com.quantummaintenance.workorder.dto.ExtraFieldsDTO;
import com.quantummaintenance.workorder.dto.PriorityEnumDTO;
import com.quantummaintenance.workorder.dto.StatusEnumDTO;
import com.quantummaintenance.workorder.dto.WorkOrderDTO;
import com.quantummaintenance.workorder.entity.*;
import com.quantummaintenance.workorder.entity.MandatoryFields;
import com.quantummaintenance.workorder.entity.PriorityEnum;
import com.quantummaintenance.workorder.entity.ShowFields;
import com.quantummaintenance.workorder.repository.ExtraFieldNameRepository;
import com.quantummaintenance.workorder.repository.WorkOrderRepository;
import com.quantummaintenance.workorder.service.WorkOrderService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;


@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping(value="/workorder")
public class WorkOrderAPI {
	
//	@Autowired
	private RestTemplate restTemplate=new RestTemplate();
	
	String customerApi="http://localhost:8080/customer/get/";
	
	private ModelMapper modelMapper=new ModelMapper();
	
	

	@Autowired
	private WorkOrderService workOrderService;
	
	@Autowired
	private ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	@GetMapping(value="/getCustomer/{companyId}")
	public ResponseEntity<Customer> getCustomerDetails(@PathVariable String companyId,@RequestHeader("Authorization") String token){
	
		HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", token);
        HttpEntity<Customer> entity = new HttpEntity<>(headers);
		
		ResponseEntity<CustomerDTO> response = restTemplate.exchange(
				customerApi+companyId,
                HttpMethod.GET,
                entity,
                CustomerDTO.class
        );
		Customer customer=modelMapper.map(response.getBody(), Customer.class);
        return ResponseEntity.ok(customer);
	
	}
	
//	@PostMapping(value="/addorder")
//	public void addWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) {
//		workOrderService.addOrder(workOrderDTO);
//	}
	@PostMapping(value="/addorder")
	public ResponseEntity<WorkOrderDTO> addWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) throws JsonMappingException, JsonProcessingException, ParseException {
		
		WorkOrderDTO myWorkOrderDTO=workOrderService.addOrder(workOrderDTO);
		return ResponseEntity.ok(myWorkOrderDTO);
	}
	@GetMapping(value="/getallorder/{companyId}")
	public List<WorkOrderDTO> getAllWorkOrder(@PathVariable String companyId){
		return workOrderService.getOrderList(companyId);
	}
	@GetMapping(value="/getworkorder/{id}")
	public WorkOrderDTO getWorkOrder(@PathVariable String id){
		return workOrderService.getWorkOrder(id);
	}
	@PutMapping(value="/update")
	public void updateWorkOrder(@RequestBody WorkOrderDTO workOrderDTO){
		workOrderService.updateWorkOrder(workOrderDTO);
	}
	@DeleteMapping(value="/delete/{id}")
	public void updateWorkOrder(@PathVariable String id){
		workOrderService.deleteWorkOrder(id);
	}
	@GetMapping(value="/getworkorderlist/{assetId}")
	public List<WorkOrderDTO> getWorkOrderFromAsset(@PathVariable String assetId){
		return workOrderService.getWorkOrderFromAssetId(assetId);
	}
	@GetMapping(value="/searchWorkorderlist/{companyId}")
	public List<WorkOrderDTO> getWorkOrderFromAsset(@PathVariable String companyId,@RequestParam(name = "data", required = true) String search,
            @RequestParam(name = "category", required = true) String category ){
				return workOrderService.searchedWorkOrder(companyId,search, category);
		
	}
	@GetMapping(value="/sortWorkorderlist/{companyId}")
	public List<WorkOrderDTO> getWorkOrderFromAsset(@PathVariable String companyId,
            @RequestParam(name = "category", required = true) String category ){
				return workOrderService.sortWorkOrder(companyId, category);
		
	}
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		workOrderService.addAssetExtraField(extraFieldNameDTO);
	}
	@GetMapping("/getExtraFieldName/{companyId}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String companyId){
		System.out.println("----------my company------------->"+companyId);
		return workOrderService.getAssetExtraField(companyId);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		workOrderService.deleteAssetExtraField(id);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		workOrderService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		workOrderService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{companyId}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String companyId){
		System.out.println("============================>"+name+companyId);
		MandatoryFields mandatoryFields=workOrderService.getMandatoryFields(name,companyId);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{companyId}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String companyId){
		ShowFields showFields=workOrderService.getShowFields(name,companyId);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{companyId}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String companyId){
		List<MandatoryFields> mandatoryFieldsList=workOrderService.getAllMandatoryFields(companyId);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{companyId}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String companyId){
		List<ShowFields> showFieldsList=workOrderService.getAllShowFields(companyId);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{companyId}")
	public void showFields(@PathVariable String name,@PathVariable String companyId){
		workOrderService.deleteShowAndMandatoryFields(companyId, name);
	}
	@GetMapping("/getExtraFieldNameValue/{companyId}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String companyId){
		return workOrderService.getextraFieldList(companyId);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		workOrderService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return workOrderService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		workOrderService.deleteExtraFields(id);
	}
	@DeleteMapping("/deleteWorkorderExtraFields/{id}")
	public void deleteWorkorderExtraFields(@PathVariable String id) throws Exception {
		workOrderService.deleteExtraFieldByWorkOrder(id);
	}
	
}
