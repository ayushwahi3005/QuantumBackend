package com.quantummaintenance.workorder.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.quantummaintenance.workorder.dto.WorkOrderDTO;
import com.quantummaintenance.workorder.service.WorkOrderService;
@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping(value="/workorder")
public class WorkOrderAPI {

	@Autowired
	private WorkOrderService workOrderService;
	@PostMapping(value="/addorder")
	public void addWorkOrder(@RequestBody WorkOrderDTO workOrderDTO) {
		workOrderService.addOrder(workOrderDTO);
	}
	@GetMapping(value="/getallorder/{email}")
	public List<WorkOrderDTO> getAllWorkOrder(@PathVariable String email){
		return workOrderService.getOrderList(email);
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
	
}
