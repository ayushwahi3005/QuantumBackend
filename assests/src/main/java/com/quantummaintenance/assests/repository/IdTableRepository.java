package com.quantummaintenance.assests.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.IdTable;

public interface IdTableRepository extends MongoRepository<IdTable,String> {
	public Optional<IdTable> findByCompanyId(String id);
}
