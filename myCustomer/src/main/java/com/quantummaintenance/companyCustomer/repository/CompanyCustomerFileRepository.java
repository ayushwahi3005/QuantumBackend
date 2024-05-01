package com.quantummaintenance.companyCustomer.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.companyCustomer.entity.CompanyCustomerFile;

public interface CompanyCustomerFileRepository  extends MongoRepository<CompanyCustomerFile,String> {
	public List<CompanyCustomerFile> findByAssetId(String assetId);
}
