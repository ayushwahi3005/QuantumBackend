package com.quantummaintenance.workorder.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.workorder.entity.ExtraFields;



public interface ExtraFieldsRepository extends MongoRepository<ExtraFields,String> {
	public List<ExtraFields>findByWorkorderId(String workorderId);
	public List<ExtraFields> findByCompanyId(String companyId);
	public List<ExtraFields> findByName(String name);
	public ExtraFields findByNameAndWorkorderId(String name,String workorderId);

}
