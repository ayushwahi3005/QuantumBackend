package com.quantummaintenance.companyCustomer.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;


import com.quantummaintenance.companyCustomer.dto.CompanyCustomerDTO;
import com.quantummaintenance.companyCustomer.dto.CompanyCustomerFileDTO;
import com.quantummaintenance.companyCustomer.dto.ExtraFieldNameDTO;
import com.quantummaintenance.companyCustomer.dto.ExtraFieldsDTO;
import com.quantummaintenance.companyCustomer.entity.CompanyCustomerFile;
import com.quantummaintenance.companyCustomer.entity.MandatoryFields;
import com.quantummaintenance.companyCustomer.entity.ShowFields;
import com.quantummaintenance.companyCustomer.exception.ExtraFieldAlreadyPresentException;

public interface CompanyCustomerService {
	
	public CompanyCustomerDTO addCustomer(CompanyCustomerDTO companyCustomerDTO);
	public CompanyCustomerDTO getCustomer(String id);
	public List<CompanyCustomerDTO> getAllCustomer(String companyId);
	public void updateCustomer(CompanyCustomerDTO companyCustomerDTO);
	public void deleteCustomer(String id);
	public List<String> getAllCustomerWithExtraColumns(String companyId);
	public List<String> searchedCompanyCustomer(String companyId,String search,String category);
	public List<String> sortCompanyCustomer(String companyId,String category);
	public void addCompanyCustomerExtraField(ExtraFieldNameDTO extraFieldNameDTO) throws ExtraFieldAlreadyPresentException;
	public List<ExtraFieldNameDTO> getCompanyCustomerExtraField(String companyId);
	public void deleteCompanyCustomerExtraField(String id);
	public void updateMandatoryFields(MandatoryFields mandatoryFields);
	public void updateShowFields(ShowFields showFields);
	public MandatoryFields getMandatoryFields(String name, String companyId);
	public ShowFields getShowFields(String name, String companyId);
	public List<MandatoryFields> getAllMandatoryFields(String companyId);
	public List<ShowFields> getAllShowFields(String companyId);
	public void deleteShowAndMandatoryFields(String companyId, String name);
	public Map<String, Map<String,String>> getextraFieldList(String companyId);
	public void addExtraFields(ExtraFieldsDTO extraFieldsDTO);
	public List<ExtraFieldsDTO> getExtraFields(String id);
	public void deleteExtraFields( String id) throws Exception;
	public void deleteExtraFieldByCompanyCustomer(String id);
	public CompanyCustomerFile addCompanyCustomerFile(MultipartFile file,String companyCustomerId) throws IOException;
	public List<CompanyCustomerFileDTO> getCompanyCustomerFile(String companyCustomerId);
	public CompanyCustomerFileDTO downloadFile(String id);
	public void deleteFile(String id);
	public CompanyCustomerDTO getCompanyCustomerByLocalId(Integer id);
	
	
	

}
