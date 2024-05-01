package com.quantummaintenance.assests.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.MandatoryFields;

public interface MandatoryFieldsRepository extends MongoRepository<MandatoryFields,String> {
	public Optional<MandatoryFields> findByNameAndCompanyId(String name,String companyId);
	public List<MandatoryFields> findByCompanyId(String companyId);

}
