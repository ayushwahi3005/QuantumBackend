package com.quantummaintenance.assests.repository;

import java.util.Optional;

import org.springframework.data.mongodb.repository.MongoRepository;

import com.quantummaintenance.assests.entity.QR;



public interface QRRepository extends MongoRepository<QR,String>{
	public Optional<QR> findByCompanyId(String companyId);
}
