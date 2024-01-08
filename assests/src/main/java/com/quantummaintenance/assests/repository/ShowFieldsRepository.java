package com.quantummaintenance.assests.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.ShowFields;

public interface ShowFieldsRepository extends MongoRepository<ShowFields,String> {
	public Optional<ShowFields> findByNameAndEmail(String name,String email);
	public List<ShowFields> findByEmail(String email);
}
