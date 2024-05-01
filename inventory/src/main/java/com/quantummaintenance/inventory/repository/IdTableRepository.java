package com.quantummaintenance.inventory.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.inventory.entity.IdTable;





public interface IdTableRepository extends MongoRepository<IdTable,String> {
	public Optional<IdTable> findByCompanyId(String id);
}
