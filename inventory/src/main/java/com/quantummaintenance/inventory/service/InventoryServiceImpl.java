package com.quantummaintenance.inventory.service;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.entity.Inventory;
import com.quantummaintenance.inventory.repository.InventoryRepository;

@Service
public class InventoryServiceImpl implements InventoryService {

	@Autowired
	InventoryRepository inventoryRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	@Override
	public void addInventory(InventoryDTO inventoryDTO) {
		// TODO Auto-generated method stub
		Inventory inventory=modelMapper.map(inventoryDTO, Inventory.class);
		inventoryRepository.save(inventory);
		
	}
	@Override
	public List<InventoryDTO> getAllInventory() {
		// TODO Auto-generated method stub
		List<InventoryDTO> inventoryListDTO=new ArrayList<>();
		List<Inventory> inventoryList=inventoryRepository.findAll();
		inventoryList.stream().forEach((x)->{
			InventoryDTO inventoryDTO=modelMapper.map(x, InventoryDTO.class);
			inventoryListDTO.add(inventoryDTO);
		});
		return inventoryListDTO;
	}
	@Override
	public InventoryDTO getInventory(String id) {
		// TODO Auto-generated method stub
		Optional<Inventory> inventory=inventoryRepository.findById(id);
		InventoryDTO inventoryDTO=modelMapper.map(inventory.get(), InventoryDTO.class);
		return inventoryDTO;
	}
	@Override
	public void deleteInventory(String id) {
		// TODO Auto-generated method stub
		inventoryRepository.deleteById(id);
		
	}

}
