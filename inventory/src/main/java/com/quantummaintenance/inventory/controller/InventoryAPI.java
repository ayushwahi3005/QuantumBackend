package com.quantummaintenance.inventory.controller;

import java.util.List;

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

import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.service.InventoryService;

@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/inventory")
public class InventoryAPI {
	@Autowired
	InventoryService inventoryService;
	
	@PostMapping("/addInventory")
	public ResponseEntity<String> addInventory(@RequestBody InventoryDTO inventoryDTO){
		inventoryService.addInventory(inventoryDTO);
		return new ResponseEntity<>("Inventory Created",HttpStatus.ACCEPTED);
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
	
}
