package com.quantummaintenance.companyCustomer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.companyCustomer.entity.ExtraFields;





public interface ExtraFieldsRepository extends MongoRepository<ExtraFields,String> {
	public List<ExtraFields>findByCompanyCustomerId(String workorderId);
	public List<ExtraFields> findByCompanyId(String companyId);
	public List<ExtraFields> findByName(String name);
	public ExtraFields findByNameAndCompanyCustomerId(String name,String companyCustomerId);

}
