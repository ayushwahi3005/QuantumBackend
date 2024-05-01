package com.quantummaintenance.inventory.controller;

import java.util.List;
import java.util.Map;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantummaintenance.inventory.dto.ExtraFieldNameDTO;
import com.quantummaintenance.inventory.dto.ExtraFieldsDTO;
import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.entity.Inventory;
import com.quantummaintenance.inventory.entity.MandatoryFields;
import com.quantummaintenance.inventory.entity.ShowFields;
import com.quantummaintenance.inventory.service.InventoryService;


@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/inventory")
public class InventoryAPI {
	@Autowired
	InventoryService inventoryService;
	
	private ModelMapper modelMapper=new ModelMapper();
	
	@PostMapping("/addInventory")
	public InventoryDTO addInventory(@RequestBody InventoryDTO inventoryDTO){
		Inventory inventory=inventoryService.addInventory(inventoryDTO);
		InventoryDTO myInventoryDTO=modelMapper.map(inventory, InventoryDTO.class);
//		return new ResponseEntity<>(myInventoryDTO,HttpStatus.ACCEPTED);
		return myInventoryDTO;
	}
	@PostMapping("/updateInventory")
	public void updateInventory(@RequestBody InventoryDTO inventoryDTO){
		inventoryService.updateInventory(inventoryDTO);
//		InventoryDTO myInventoryDTO=modelMapper.map(inventory, InventoryDTO.class);
//		return new ResponseEntity<>(myInventoryDTO,HttpStatus.ACCEPTED);
//		return myInventoryDTO;
	}
	@GetMapping("/getAllInventory/{companyId}")
	public ResponseEntity<List<InventoryDTO>> getAllInventory(@PathVariable String companyId){
		List<InventoryDTO> inventoryDTOList=inventoryService.getAllInventory(companyId);
		return new ResponseEntity<>(inventoryDTOList,HttpStatus.ACCEPTED);
	}
	@GetMapping("/getInventory/{id}")
	public ResponseEntity<InventoryDTO> getInventory(@PathVariable String id){
		InventoryDTO inventoryDTO=inventoryService.getInventory(id);
		return new ResponseEntity<>(inventoryDTO,HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/deleteInventory/{id}")
	public void deleteInventory(@PathVariable String id){
		inventoryService.deleteInventory(id);
	}
	
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		inventoryService.addInventoryExtraField(extraFieldNameDTO);
	}
	@GetMapping("/getExtraFieldName/{companyId}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String companyId){
		System.out.println("----------my company------------->"+companyId);
		return inventoryService.getInventoryExtraField(companyId);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		inventoryService.deleteInventoryExtraField(id);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		inventoryService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		inventoryService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{companyId}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String companyId){
		System.out.println("============================>"+name+companyId);
		MandatoryFields mandatoryFields=inventoryService.getMandatoryFields(name,companyId);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{companyId}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String companyId){
		ShowFields showFields=inventoryService.getShowFields(name,companyId);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{companyId}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String companyId){
		List<MandatoryFields> mandatoryFieldsList=inventoryService.getAllMandatoryFields(companyId);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{companyId}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String companyId){
		List<ShowFields> showFieldsList=inventoryService.getAllShowFields(companyId);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{companyId}")
	public void showFields(@PathVariable String name,@PathVariable String companyId){
		inventoryService.deleteShowAndMandatoryFields(companyId, name);
	}
	@GetMapping("/getExtraFieldNameValue/{companyId}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String companyId){
		return inventoryService.getExtraFieldList(companyId);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		inventoryService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return inventoryService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		inventoryService.deleteExtraFields(id);
	}
	@DeleteMapping("/deleteInventoryExtraFields/{id}")
	public void deleteInventoryExtraFields(@PathVariable String id) throws Exception {
		inventoryService.deleteExtraFieldByInventory(id);
	}
	@GetMapping("/allInventoryWithExtraFields/{companyId}")
	public List<String> getAllInventoryList(@PathVariable String companyId){
		return inventoryService.inventoryAllData( companyId);
	}
	
}
