package com.quantummaintenance.companyCustomer.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.companyCustomer.entity.CompanyCustomer;

public interface CompanyCustomerRepository extends MongoRepository<CompanyCustomer,String>{
	
	public List<CompanyCustomer> findByCompanyId(String id);
	public Optional<CompanyCustomer> findByCompanyCustomerId(Integer id);

}
