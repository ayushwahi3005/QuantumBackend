package com.quantummaintenance.workorder.service;

import java.util.List;
import java.util.Optional;
import java.time.LocalDate;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantummaintenance.workorder.dto.WorkOrderDTO;
import com.quantummaintenance.workorder.entity.StatusEnum;
import com.quantummaintenance.workorder.entity.WorkOrder;
import com.quantummaintenance.workorder.repository.WorkOrderRepository;

@Service
public class WorkOrderServiceImpl implements WorkOrderService {

	@Autowired
	private WorkOrderRepository workOrderRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	@Override
	public void addOrder(WorkOrderDTO workOrderDTO) {
		// TODO Auto-generated method stub
		//System.out.println("------------------Order done--------------------->");
		WorkOrder workOrder=modelMapper.map(workOrderDTO, WorkOrder.class);
		workOrder.setStatus(StatusEnum.open);
		workOrder.setLastUpdate(LocalDate.now());
		workOrderRepository.save(workOrder);
		
	}
	@Override
	public List<WorkOrderDTO> getOrderList(String email) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByEmail(email);
		//System.out.println("--------------------------------------->"+workOrderList.size());
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
	}
	@Override
	public void updateWorkOrder(WorkOrderDTO workOrderDTO) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(workOrderDTO.getId());
		WorkOrder workOrder= modelMapper.map(workOrderDTO, WorkOrder.class);
		workOrder.setLastUpdate(LocalDate.now());
		workOrderRepository.save(workOrder);
		
	}
	@Override
	public void deleteWorkOrder(String id) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(id);
		workOrderRepository.delete(workOrderOptional.get());
		
	}
	@Override
	public WorkOrderDTO getWorkOrder(String id) {
		// TODO Auto-generated method stub
		Optional<WorkOrder> workOrderOptional=workOrderRepository.findById(id);
		WorkOrderDTO workOrderDTO= modelMapper.map(workOrderOptional.get(), WorkOrderDTO.class);
		return workOrderDTO;
		
	}
	@Override
	public List<WorkOrderDTO> getWorkOrderFromAssetId(String assetId) {
		// TODO Auto-generated method stub
		List<WorkOrder> workOrderList=workOrderRepository.findByAssetId(assetId);
		List<WorkOrderDTO> workOrderDTOList=new ArrayList<>();
		workOrderList.stream().forEach((x)->{
			WorkOrderDTO workOrderDTO=modelMapper.map(x, WorkOrderDTO.class);
			workOrderDTOList.add(workOrderDTO);
		});
		return workOrderDTOList;
	}

}
