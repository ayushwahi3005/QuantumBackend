package com.quantummaintenance.assests.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Date;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;

import java.io.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;

import com.quantummaintenance.assests.dto.*;
import com.quantummaintenance.assests.entity.Assets;
import com.quantummaintenance.assests.entity.ExtraFieldName;
import com.quantummaintenance.assests.entity.MandatoryFields;
import com.quantummaintenance.assests.entity.QR;
import com.quantummaintenance.assests.entity.ShowFields;
import com.quantummaintenance.assests.entity.ExtraFields;
import com.quantummaintenance.assests.repository.AssetsRepository;
import com.quantummaintenance.assests.repository.ExtraFieldNameRepository;
import com.quantummaintenance.assests.repository.ExtraFieldsRepository;
import com.quantummaintenance.assests.service.AssetsService;
import com.quantummaintenance.assests.exception.*;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.mail.javamail.MimeMessageHelper;

//@CrossOrigin("http://localhost:4200/")
//@CrossOrigin("*")
@RestController
@RequestMapping("/assets")
public class AssetsAPI {
	
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	private AssetsService assetsService;
	
	@Autowired
	private ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	private ExtraFieldsRepository extraFieldsRepository;
	
	@Autowired
	private AssetsRepository assetsRepository;
	
	
	
	 private ModelMapper modelMapper=new ModelMapper();
	
	RestTemplate restTemplate = new RestTemplate();
	
	@GetMapping("/working")
	public String working(){
		return "Working";
	}
	
	@GetMapping("/{companyId}")
	public List<AssetsDTO> getAssets(@PathVariable String companyId){
		return assetsService.getAssetsDetails(companyId);
	}
	@GetMapping("/getByCutomerId/{customerId}")
	public List<AssetsDTO> getAssetsByCustomer(@PathVariable String customerId){
		return assetsService.getAssetsDetailsByCustomerId(customerId);
	}
	@PutMapping("/addassets")
	public void addAssets(@RequestBody AssetsDTO assestsDTO) {
		assetsService.addAssets(assestsDTO);
	}
	@PostMapping("/addNewAssets")
	public ResponseEntity<AssetsDTO> addNewAssets(@RequestBody AssetsDTO assestsDTO) {
		AssetsDTO assetsDTO= assetsService.addAssets(assestsDTO);
		return ResponseEntity.ok(assetsDTO);
	}
	@PostMapping("/import/{companyId}/{email}")
	public void importFile(@RequestParam("file") MultipartFile file,@RequestParam("columnMappings") String columnMappings,@PathVariable String companyId,@PathVariable String email) throws CsvValidationException, MessagingException,ImportFileRowException {
//		System.out.println("------||---------------------------------------/////////////////////////////////////------->"+columnMappings);
		HttpHeaders myheaders = new HttpHeaders();
//TO BE IMPLEMENTED WHEN AUTHORIZATION IS ADDED		myheaders.set("Authorization", token);
        HttpEntity<CompanyCustomerDTO> entity = new HttpEntity<>(myheaders);
        String companyCustomerApi="http://localhost:8085/companycustomer/getCompanyCustomerByLocalId/";
        
		Map<String,String> columnMap=new HashMap<>();
	 try {
            // Create a JsonFactory and a JsonParser
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(columnMappings);

            // Loop through JSON tokens
            String key="",val="";
            while (!jsonParser.isClosed()) {
                // Get the current token
                JsonToken jsonToken = jsonParser.nextToken();
                if(jsonToken==null) {
                	break;
                }


                if(key.equals("")==false) {
                	columnMap.put(key, val);
                }
                switch (jsonToken) {
                    case START_OBJECT:
                        System.out.println("Start of object");
                        break;
                    case FIELD_NAME:
                        System.out.println("Field name: " + jsonParser.getCurrentName());
                        key=jsonParser.getCurrentName();
                        break;
                    case VALUE_STRING:
                        System.out.println("Field value: " + jsonParser.getText());
                        val=jsonParser.getText();
                        
                        break;
                    case END_OBJECT:
                        System.out.println("End of object");
                        break;
				default:
					break;
				
                }
            }

            // Close the JsonParser
            jsonParser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	   System.out.println("--------------> "+columnMap.size());
		List<AssetsDTO> assetList=new ArrayList<AssetsDTO>(); 
		
		try (InputStream inputStream = file.getInputStream();
		         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
			  if(reader.lines().count()>5001) {
				  throw new ImportFileRowException("Import File cannot import more than 5000 rows");
			  }
		       
		    } catch (IOException e) {
		        // Handle IOException
		        e.printStackTrace();
		       
		    }
	try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
	         CSVReader csvReader = new CSVReader(reader)) {

	        String[] headers = csvReader.readNext();
	        Map<Integer, String> headerMap = new HashMap<>();

	        if (headers != null) {
	            for (int i = 0; i < headers.length; i++) {
	                headerMap.put(i, headers[i]);
	            }
	        }

	        
	        
	        
	        
	        
	        
	        
	        
	        

	        String[] row;
	        long ind=0;
	        Workbook workbook = new XSSFWorkbook();

	        // Create a Sheet
	        Sheet sheet = workbook.createSheet("Sheet1");
	        int excelIndex=0;
	        while ((row = csvReader.readNext()) != null) {
	        	Row myrow= sheet.createRow(excelIndex);
	        	 Cell cell1 = myrow.createCell(0);
     	        cell1.setCellValue("Line "+(int)(ind+1));
	            AssetsDTO assetsDTO = new AssetsDTO();
	            assetsDTO.setCompanyId(companyId);
	            int errorFlag=0;
	            String errorDesc="";
//	            System.out.println("-------|||||---errorFlag----> "+errorFlag);
	            for (int j = 0; j < row.length; j++) {
	                String field = headerMap.get(j);
//	                System.out.println("--------------> "+j+" " + row[j]);
//	                System.out.println("-------|||||-------> "+columnMap.get(field).toLowerCase());

	                if(errorFlag==1) {
	                	break;
	                }
	                
	                
	                
	                switch ( columnMap.get(field).toLowerCase()) {
	                
	                

	                case "name":
	                    assetsDTO.setName(row[j]);
	                    break;
	                    
	                case "serialnumber":
	                    assetsDTO.setSerialNumber(row[j]);
	                    break;
	                case "category":
	                    assetsDTO.setCategory(row[j]);
	                    break;
	                case "customer":
	                	ResponseEntity<CompanyCustomerDTO> response = restTemplate.exchange(
	                			companyCustomerApi+row[j],
	                            HttpMethod.GET,
	                            entity,
	                            CompanyCustomerDTO.class
	                    );
	                	if(response.getBody()==null) {
	                		System.out.println("ERROR WHILE ADDING IN CUSTOMER for row->"+ind);
	                		  

	                	        // Create Cells and set values
	                		  errorDesc+="ERROR WHILE ADDING IN CUSTOMER";
	                	       

	                	        
	                		errorFlag=1;
	                		break;
	                	}
	                	else {
	                	CompanyCustomerDTO companyCustomerDTO=modelMapper.map(response.getBody(), CompanyCustomerDTO.class);
	                  
	                    assetsDTO.setCustomerId(companyCustomerDTO.getId());
	                    assetsDTO.setCustomer(companyCustomerDTO.getName());
	                  
	                    break;
	                	}
	                case "location":
	                    assetsDTO.setLocation(row[j]);
	                    break;
	                case "status":
               	
	                	if((row[j].toLowerCase().equals("active"))||(row[j].toLowerCase().equals("inactive"))||(row[j].toLowerCase().equals("outofservice"))) {

	                		 assetsDTO.setStatus(row[j]);
	                		 errorFlag=0;
	 	                    break;
	                		
	                	}
	                	else {
	                		System.out.println("ERROR WHILE ADDING IN Status for line->"+ind);
	                		if(errorDesc.length()>0) {
	                			errorDesc+=", ";
	                		}
	                		errorDesc+="ERROR WHILE ADDING IN STATUS";
	                		errorFlag=1;
	                		break;
	                	}
	         

	                

	            }

	                
	            
	            }
//	            System.out.println("///'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/  reached errorFlag" +errorFlag);
	            if(errorFlag==0) {
//	            	System.out.println("///'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/");
	            	 AssetsDTO mynewAsset=assetsService.addAssets(assetsDTO);
		            
		            //-------------------------------------------
		            for (int j = 0; j < row.length; j++) {
		                String field = headerMap.get(j);
//		                System.out.println("-------|||||-------> "+columnMap.get(field).toLowerCase());
	
		                
		                
		                
		                
		               
		                
		                String value=row[j];
		                
		               List<ExtraFieldName> listExtraFieldName= extraFieldNameRepository.findByCompanyId(companyId);
		               
		               for(int i=0;i<listExtraFieldName.size();i++) {
		            	   if(columnMap.get(field).toLowerCase().equals(listExtraFieldName.get(i).getName().toLowerCase())) {
		            		   System.out.println("-----------------working ---->"+listExtraFieldName.get(i).getType());
		            		   ExtraFields extraFieldsDTO=new ExtraFields(); 
		            		   extraFieldsDTO.setAssetId(mynewAsset.getId());
		            		   extraFieldsDTO.setName(listExtraFieldName.get(i).getName());
		            		   extraFieldsDTO.setType(listExtraFieldName.get(i).getType());
		            		  
		            		   extraFieldsDTO.setCompanyId(companyId);
		            		   if(listExtraFieldName.get(i).getType().equals("number")) {
		            			   try {
		            				   Integer val=Integer.parseInt(value);
		            				   System.out.println("-----------------extra---->"+val+"->"+value);
		            				   extraFieldsDTO.setValue(val.toString()); 
		            			   }
		            			   catch(Exception e) {
		            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind+1);
		            				 errorFlag=1;
				                		if(errorDesc.length()>0) {
				                			errorDesc+=", ";
				                		}
				                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
		            			   }
		            		   }
		            		   if(listExtraFieldName.get(i).getType().equals("date")) {
		            			   try {
		            				
		            				   DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
		            			        LocalDate date = LocalDate.parse(value, inputFormatter);

		            			        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		            			        String formattedDate = date.format(outputFormatter);
		            				  
		            				   System.out.println("-----------------extra-date--->"+formattedDate+"->"+value);
		            				   extraFieldsDTO.setValue(formattedDate); 
		            			   }
		            			   catch(Exception e) {
		            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind+1);
		            				 errorFlag=1;
				                		if(errorDesc.length()>0) {
				                			errorDesc+=", ";
				                		}
				                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
		            			   }
		            		   }
		            		   else {
		            			   extraFieldsDTO.setValue(value); 
		            		   }
		            		   if(errorFlag==0) {
		            		   extraFieldsRepository.save(extraFieldsDTO);
		            		   }
		            		   else {
		            			   Assets myAsset=modelMapper.map(mynewAsset, Assets.class);
		            			   assetsRepository.delete(myAsset);
		            			   
		            		   }
		            	   }
		               }
	
//		               listExtraFieldName.stream().forEach((x)->{
//		            	   
//		            	   
//		               });
		                
		            
		            }
	            }
	           

	          
	            Cell cell2 = myrow.createCell(1);
    	        cell2.setCellValue(errorDesc);
    	        if(errorFlag==1) {
    	        	System.out.println("Inside errorFLag");
    	       
    	        // Close the workbook to release resources
    	        excelIndex++;
    	       
    	        }
	            ind++;
	            
	        }
	        if(excelIndex>0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("Report.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, We have attached import result below");
	            helper.addAttachment("AssetAttachment.xlsx", new File("Report.xlsx"));

	            emailSender.send(message);
	        }
	        if(excelIndex==0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("Report.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, Your Import was Successfully Done");
//	            helper.addAttachment("ExcelAttachment.xlsx", new File("Report.xlsx"));

	            emailSender.send(message);
	        }

	        
//	        System.out.println("-------|||||-------> "+assetList.size());
//	        assetsService.importExcel(assetList);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	 
		

		
		
	}
	@PostMapping("/importUpdation/{companyId}/{email}")
	public void updateAssetWithFile(@RequestParam("file") MultipartFile file,@RequestParam("columnMappings") String columnMappings,@PathVariable String companyId,@PathVariable String email) throws CsvValidationException, JsonParseException, IOException, MessagingException,ImportFileRowException {
		HttpHeaders myheaders = new HttpHeaders();
		//TO BE IMPLEMENTED WHEN AUTHORIZATION IS ADDED		myheaders.set("Authorization", token);
		        HttpEntity<CompanyCustomerDTO> entity = new HttpEntity<>(myheaders);
		        String companyCustomerApi="http://localhost:8085/companycustomer/getCompanyCustomerByLocalId/";
		
		System.out.println("------||-------->"+columnMappings);
		Map<String,String> columnMap=new HashMap<>();
	 try {
            // Create a JsonFactory and a JsonParser
            JsonFactory jsonFactory = new JsonFactory();
            JsonParser jsonParser = jsonFactory.createParser(columnMappings);

            // Loop through JSON tokens
            String key="",val="";
            while (!jsonParser.isClosed()) {
                // Get the current token
                JsonToken jsonToken = jsonParser.nextToken();
                if(jsonToken==null) {
                	break;
                }


                if(key.equals("")==false) {
                	columnMap.put(key, val);
                }
                switch (jsonToken) {
                    case START_OBJECT:
                        System.out.println("Start of object");
                        break;
                    case FIELD_NAME:
                        System.out.println("Field name: " + jsonParser.getCurrentName());
                        key=jsonParser.getCurrentName();
                        break;
                    case VALUE_STRING:
                        System.out.println("Field value: " + jsonParser.getText());
                        val=jsonParser.getText();
                        
                        break;
                    case END_OBJECT:
                        System.out.println("End of object");
                        break;
				default:
					break;
				
                }
            }

            // Close the JsonParser
            jsonParser.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
	 try (InputStream inputStream = file.getInputStream();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
		  if(reader.lines().count()>5001) {
			  throw new ImportFileRowException("Import File cannot import more than 5000 rows");
		  }
	       
	    } catch (IOException e) {
	        // Handle IOException
	        e.printStackTrace();
	       
	    }
		List<AssetsDTO> assetList=new ArrayList<AssetsDTO>(); 
	try (InputStreamReader reader = new InputStreamReader(file.getInputStream());
	         CSVReader csvReader = new CSVReader(reader)) {

	        String[] headers = csvReader.readNext();
	        Map<Integer, String> headerMap = new HashMap<>();

	        if (headers != null) {
	            for (int i = 0; i < headers.length; i++) {
	                headerMap.put(i, headers[i]);
	            }
	        }
	 

	        String[] row;
	        long ind=0;
	        Workbook workbook = new XSSFWorkbook();

	        // Create a Sheet
	        Sheet sheet = workbook.createSheet("Sheet1");
	        int excelIndex=0;
	        while ((row = csvReader.readNext()) != null) {
	        	Row myrow= sheet.createRow(excelIndex);
	        	 Cell cell1 = myrow.createCell(0);
	     	        cell1.setCellValue("Line "+(int)(ind+1));
	        	int errorFlag=0;
	        	  String errorDesc="";
//	            AssetsDTO assetsDTO = new AssetsDTO();
//	            assetsDTO.setCompanyId(companyId);
	        	 Assets assets=new Assets();
	            for (int j = 0; j < row.length; j++) {
	            	if(j>0&&assets==null) {
	            		break;
	            	}
	                String field = headerMap.get(j);
//	                System.out.println("--------------> "+j+" " + row[j]);
//	                System.out.print("-------|||||-------> "+columnMap.get(field).toLowerCase());

	                
	                
	                
	               
	                switch ( columnMap.get(field).toLowerCase()) {
	                case "assetid":
	                    String assetIdValue = row[j].trim();
	                    System.out.println("assetId------------------->"+assetIdValue);
//	                    assetIdValue = assetIdValue.substring(0, assetIdValue.length() - 2);
//	                    try {
//	                        if (!assetIdValue.isEmpty()) {
//	                            assetsDTO.setAssetId(Integer.parseInt(assetIdValue));
//	                        } else {
//	                            // Handle empty cell case, set a default value, or take appropriate action
//	                        }
//	                    } catch (NumberFormatException e) {
//	                        // Handle the exception or log an error message
//	                        System.err.println("Error parsing integer: " + e.getMessage());
//	                        // Set a default value or take appropriate action
//	                    }
	                    assets=assetsRepository.findByAssetIdAndCompanyId(Integer.parseInt(assetIdValue),companyId);
//	                    System.out.println("------------------/////////----"+assets.getId());
//	                    assetsDTO.setId(assets.getId());
//	                    System.out.println("------------------/////////----"+assetsDTO.getId());
	                    if(assets==null) {
	                    	errorFlag=1;
	                    	 errorDesc+="ERROR WHILE UPDATING IN ASSETID";
	                    }
	                    break;
	                

	                case "name":
	                	
	                	assets.setName(row[j]);
	                	
	                    break;
	                    
	                case "serialnumber":
	                
	                	assets.setSerialNumber(row[j]);
	                    break;
	                case "category":
	                
	                	assets.setCategory(row[j]);
	                    break;
	                case "customer":
	                	
	                	ResponseEntity<CompanyCustomerDTO> response = restTemplate.exchange(
	                			companyCustomerApi+row[j],
	                            HttpMethod.GET,
	                            entity,
	                            CompanyCustomerDTO.class
	                    );
	                	if(response.getBody()==null) {
	                		System.out.println("ERROR WHILE UPDATING IN CUSTOMER");
	                		 errorDesc+="ERROR WHILE UPDATING IN CUSTOMER";
	                	      

	                		errorFlag=1;
	                		
	                	}
	                	else {
	                	CompanyCustomerDTO companyCustomerDTO=modelMapper.map(response.getBody(), CompanyCustomerDTO.class);
	                  
	                	assets.setCustomerId(companyCustomerDTO.getId());
	                	assets.setCustomer(companyCustomerDTO.getName());
	                  
	                   
	                	}
	                	break;
	                case "location":
	                	assets.setLocation(row[j]);
	                    break;
	                case "status":
	                	if((row[j].toLowerCase().equals("active"))||(row[j].toLowerCase().equals("inactive"))||(row[j].toLowerCase().equals("outofservice"))) {

	                		assets.setStatus(row[j]);
	                		 errorFlag=0;
	 	                    break;
	                		
	                	}
	                	else {
	                		if(errorDesc.length()>0) {
	                			errorDesc+=", ";
	                		}
	                		errorDesc+="ERROR WHILE UPDATING IN STATUS";
	                		errorFlag=1;
	                		break;
	                	}
	         

	                

	            }
	                if(errorFlag==0) {
	                String value=row[j];
	                
		               List<ExtraFieldName> listExtraFieldName= extraFieldNameRepository.findByCompanyId(companyId);
		               String id=assets.getId();
  
		               
		               for(int i=0;i<listExtraFieldName.size();i++) {
		            	   if(columnMap.get(field).toLowerCase().equals(listExtraFieldName.get(i).getName().toLowerCase())) {
		            		   ExtraFields extraFieldsDTO=new ExtraFields(); 
		            		   Optional<ExtraFields> extraFieldsOptional=extraFieldsRepository.findByNameAndAssetId(listExtraFieldName.get(i).getName(), id);
		            		   System.out.println("-----------------working ---->"+listExtraFieldName.get(i).getType());
		            		   if(extraFieldsOptional.isPresent()) {
			            		   extraFieldsDTO.setId(extraFieldsOptional.get().getId());
			            		   extraFieldsDTO.setAssetId(id);
			            		   extraFieldsDTO.setName(listExtraFieldName.get(i).getName());
			            		   extraFieldsDTO.setType(listExtraFieldName.get(i).getType());
			            		   if(listExtraFieldName.get(i).getType().equals("number")) {
			            			   try {
			            				   Integer val=Integer.parseInt(value);
			            				   System.out.println("-----------------extra---->"+val+"->"+value);
			            				   extraFieldsDTO.setValue(val.toString()); 
			            			   }
			            			   catch(Exception e) {
			            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind);
			            				 errorFlag=1;
					                		if(errorDesc.length()>0) {
					                			errorDesc+=", ";
					                		}
					                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
			            			   }
			            		   }
			            		   else if(listExtraFieldName.get(i).getType().equals("date")) {
			            			   try {
			            				
			            				   DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
			            			        LocalDate date = LocalDate.parse(value, inputFormatter);

			            			        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
			            			        String formattedDate = date.format(outputFormatter);
			            				  
			            				   System.out.println("-----------------extra-date--->"+formattedDate+"->"+value);
			            				   extraFieldsDTO.setValue(formattedDate); 
			            			   }
			            			   catch(Exception e) {
			            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind);
			            				 errorFlag=1;
					                		if(errorDesc.length()>0) {
					                			errorDesc+=", ";
					                		}
					                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
			            			   }
			            		   }
				            		else {
				            			   extraFieldsDTO.setValue(value); 
				            		   }
				            		  
			            		   }
			            		   else {
			            			   extraFieldsDTO.setAssetId(id);
				            		   extraFieldsDTO.setName(listExtraFieldName.get(i).getName());
				            		   extraFieldsDTO.setType(listExtraFieldName.get(i).getType());
				            		   if(listExtraFieldName.get(i).getType().equals("number")) {
				            			   try {
				            				   Integer val=Integer.parseInt(value);
				            				   System.out.println("-----------------extra---->"+val+"->"+value);
				            				   extraFieldsDTO.setValue(val.toString()); 
				            			   }
				            			   catch(Exception e) {
				            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind+1);
				            				 errorFlag=1;
						                		if(errorDesc.length()>0) {
						                			errorDesc+=", ";
						                		}
						                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
				            			   }
				            		   }
				            		   else if(listExtraFieldName.get(i).getType().equals("date")) {
				            			   try {
				            				
				            				   DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
				            			        LocalDate date = LocalDate.parse(value, inputFormatter);

				            			        DateTimeFormatter outputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				            			        String formattedDate = date.format(outputFormatter);
				            				  
				            				   System.out.println("-----------------extra-date--->"+formattedDate+"->"+value);
				            				   extraFieldsDTO.setValue(formattedDate); 
				            			   }
				            			   catch(Exception e) {
				            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind+1);
				            				 errorFlag=1;
						                		if(errorDesc.length()>0) {
						                			errorDesc+=", ";
						                		}
						                		errorDesc+="ERROR WHILE ADDING IN "+listExtraFieldName.get(i).getName().toUpperCase();
				            			   }
				            		   }
				            		   else {
				            			   extraFieldsDTO.setValue(value); 
				            		   }
				            		   extraFieldsDTO.setCompanyId(companyId);
				            		  
			            		   }
		            		  
		            		 
		            		   
		            		   if(errorFlag==0) {
		            			   System.out.println("Saving Row:"+(int)(ind+1));
		            		   extraFieldsRepository.save(extraFieldsDTO);
		            		   }
		            	   }
		               }
	                }
	            
	            }
	            
	            if(errorFlag==0) {
	            AssetsDTO assetsDTO = modelMapper.map(assets, AssetsDTO.class);
	            assetsService.addAssets(assetsDTO);
	            //-------------------------------------------
	            }
	           

//	            System.out.println();
	            Cell cell2 = myrow.createCell(1);
    	        cell2.setCellValue(errorDesc);
    	        if(errorFlag==1) {
    	        	System.out.println("Inside errorFLag");
    	       
    	        // Close the workbook to release resources
    	        excelIndex++;
    	       
    	        }
	            ind++;
	            
	            
	        }
	        if(excelIndex>0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("Report.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, We have attached import result below");
	            helper.addAttachment("AssetAttachment.xlsx", new File("Report.xlsx"));

	            emailSender.send(message);
	        }
	        if(excelIndex==0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("Report.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, Your Update was Successfully Done");
//	            helper.addAttachment("ExcelAttachment.xlsx", new File("Report.xlsx"));

	            emailSender.send(message);
	        }


	        System.out.println("-------|||||-------> "+assetList.size());
//	        assetsService.importExcel(assetList);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	@PostMapping("/imageUpload")
	public void importFile(@RequestBody AssetImageDTO assetImageDTO) throws Exception {
		
		assetsService.addImage(assetImageDTO);
		
		
	}
	@PostMapping("/removeImage")
	public void removeImage(@RequestBody String id) throws Exception {
		
		
		assetsService.removeImage(id);
		
		
	}
	@PostMapping("/removeAsset")
	public void removeAsset(@RequestBody String id) throws Exception {
		
		
		assetsService.removeAsset(id);
		
		
	}
	@GetMapping("/getAsset/{id}")
	public AssetsDTO getAsset(@PathVariable String id) throws Exception {
		return assetsService.getAsset(id);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		assetsService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return assetsService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		assetsService.deleteExtraFields(id);
	}
	@GetMapping("/getExtraFieldName/{companyId}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String companyId){
		System.out.println("----------my company------------->"+companyId);
		return assetsService.getAssetExtraField(companyId);
	}
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		assetsService.addAssetExtraField(extraFieldNameDTO);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		assetsService.deleteAssetExtraField(id);
	}
	@GetMapping("/getExtraFieldNameValue/{companyId}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String companyId){
		return assetsService.getextraFieldList(companyId);
	}
	@PostMapping("/addCheckInOut")
	public void addCheckInOut(@RequestBody CheckInDTO checkInDTO){
		assetsService.addCheckInOut(checkInDTO);
	}
		
	@GetMapping("/getCheckInOutList/{assetId}")
	public ResponseEntity<List<CheckInOutDTO>> getCheckInOutList(@PathVariable String assetId){
		List<CheckInOutDTO> checkInOutList=assetsService.getCheckOutInList(assetId);
		return new ResponseEntity<>(checkInOutList,HttpStatus.ACCEPTED);
	}
	@PostMapping("/addFile/{assetId}")
	public ResponseEntity<ResponseMessageDTO> addAssetFile(@RequestParam("file") MultipartFile file,@PathVariable String assetId) {
		System.out.println("------------------------inside Multifile------------->");
		String message = "";
		try {
			assetsService.addAssetFile(file,assetId);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			ResponseMessageDTO responseMessageDTO=new ResponseMessageDTO();
			responseMessageDTO.setResponseMessage(message);
		      return new ResponseEntity<>(responseMessageDTO,HttpStatus.ACCEPTED);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			ResponseMessageDTO responseMessageDTO=new ResponseMessageDTO();
			responseMessageDTO.setResponseMessage(message);
		      return new ResponseEntity<>(responseMessageDTO,HttpStatus.EXPECTATION_FAILED);
		}
	}
	@GetMapping("/getFile/{assetId}")
	public List<AssetFileDTO> getAssetFile(@PathVariable String assetId) {
		return assetsService.getAssetFile(assetId);
	}
	@GetMapping("/getFile/download/{id}")
	public ResponseEntity<?> downloadFile(@PathVariable String id) {
		AssetFileDTO assetFileDTO=assetsService.downloadFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
		return ResponseEntity.status(HttpStatus.OK).contentType(MediaType.valueOf("json/object")).body(assetFileDTO.getFile());
	}
	@DeleteMapping("deleteFile/{id}")
	public void deleteFile(@PathVariable String id) {
		assetsService.deleteFile(id);
//		return new ResponseEntity<>(assetFileDTO.getFile(),HttpStatus.OK);
//		return new ResponseEntity<>("Successfully Deleted File",HttpStatus.EXPECTATION_FAILED);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		assetsService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		assetsService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{companyId}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String companyId){
		System.out.println("============================>"+name+companyId);
		MandatoryFields mandatoryFields=assetsService.getMandatoryFields(name,companyId);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{companyId}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String companyId){
		ShowFields showFields=assetsService.getShowFields(name,companyId);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{companyId}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String companyId){
		List<MandatoryFields> mandatoryFieldsList=assetsService.getAllMandatoryFields(companyId);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{companyId}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String companyId){
		List<ShowFields> showFieldsList=assetsService.getAllShowFields(companyId);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{companyId}")
	public void showFields(@PathVariable String name,@PathVariable String companyId){
		assetsService.deleteShowAndMandatoryFields(companyId, name);
	}
	@PostMapping("/saveQRData")
	public void saveQRData(@RequestBody QR qr){
		assetsService.qrDataUpdation(qr);
	}
	@GetMapping("/getQRData/{companyId}")
	public ResponseEntity<QR> getQRData(@PathVariable String companyId){
		QR qr=assetsService.getQRData(companyId);
		return ResponseEntity.ok(qr);
	}
	
	@GetMapping("/getAllAssetData/{companyId}")
	public PaginatedResultDTO<String> getAllAssetData(@PathVariable String companyId){
		return assetsService.getAllAssetDetails(companyId);
	}
	
	@GetMapping(value="/searchAssetlist/{companyId}")
	public List<String> getWorkOrderFromAsset(@PathVariable String companyId,@RequestParam(name = "data", required = true) String search,
            @RequestParam(name = "category", required = true) String category ){
		System.out.println("----------my workorder search------------->"+search);
				return assetsService.searchedAssets(companyId,search, category);
		
	}
	@GetMapping(value="/sortAssetlist/{companyId}")
	public List<String> getWorkOrderFromAsset(@PathVariable String companyId,
            @RequestParam(name = "category", required = true) String category ){
				return assetsService.sortAssets(companyId, category);
		
	}
	
	@PostMapping("/updateAssetsWithInActive")
	public void updateAssetsWithInActive(@RequestBody String customerId) {
		assetsService.updateAssetsWithInActive(customerId);
		
	}
	
	@PostMapping("/advanceFilter/{pageNumber}/{pageSize}")
	public PaginatedResultDTO<String> advanceFilter(@RequestBody  Object filter,@PathVariable Integer pageNumber,@PathVariable Integer pageSize) {
		System.out.print(pageNumber);
		return assetsService.advanceFilter(filter,pageNumber,pageSize);
		
	}

	
	
}
