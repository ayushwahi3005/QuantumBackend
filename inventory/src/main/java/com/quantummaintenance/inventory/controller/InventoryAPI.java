package com.quantummaintenance.inventory.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import java.io.*;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import com.quantummaintenance.inventory.dto.ExtraFieldNameDTO;
import com.quantummaintenance.inventory.dto.ExtraFieldsDTO;
import com.quantummaintenance.inventory.dto.InventoryDTO;
import com.quantummaintenance.inventory.entity.*;
import com.quantummaintenance.inventory.entity.Inventory;
import com.quantummaintenance.inventory.entity.MandatoryFields;
import com.quantummaintenance.inventory.entity.ShowFields;
import com.quantummaintenance.inventory.service.InventoryService;
import com.quantummaintenance.inventory.repository.*;
import com.quantummaintenance.inventory.exception.ImportFileRowException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;


@CrossOrigin("http://localhost:4200/")
@RestController
@RequestMapping("/inventory")
public class InventoryAPI {
	
	@Autowired
	private JavaMailSender emailSender;
	
	@Autowired
	InventoryService inventoryService;
	
	@Autowired
	private ExtraFieldNameRepository extraFieldNameRepository;
	
	@Autowired
	private ExtraFieldsRepository extraFieldsRepository;
	
	@Autowired
	private InventoryRepository inventoryRepository;
	
	private ModelMapper modelMapper=new ModelMapper();
	
	@PostMapping("/addInventory")
	public InventoryDTO addInventory(@RequestBody InventoryDTO inventoryDTO){
		Inventory inventory=inventoryService.addInventory(inventoryDTO);
		InventoryDTO myInventoryDTO=modelMapper.map(inventory, InventoryDTO.class);
//		return new ResponseEntity<>(myInventoryDTO,HttpStatus.ACCEPTED);
		return myInventoryDTO;
	}
	@PostMapping("/updateInventory")
	public void updateInventory(@RequestBody InventoryDTO inventoryDTO){
		inventoryService.updateInventory(inventoryDTO);
//		InventoryDTO myInventoryDTO=modelMapper.map(inventory, InventoryDTO.class);
//		return new ResponseEntity<>(myInventoryDTO,HttpStatus.ACCEPTED);
//		return myInventoryDTO;
	}
	@GetMapping("/getAllInventory/{companyId}")
	public ResponseEntity<List<InventoryDTO>> getAllInventory(@PathVariable String companyId){
		List<InventoryDTO> inventoryDTOList=inventoryService.getAllInventory(companyId);
		return new ResponseEntity<>(inventoryDTOList,HttpStatus.ACCEPTED);
	}
	@GetMapping("/getInventory/{id}")
	public ResponseEntity<InventoryDTO> getInventory(@PathVariable String id){
		InventoryDTO inventoryDTO=inventoryService.getInventory(id);
		return new ResponseEntity<>(inventoryDTO,HttpStatus.ACCEPTED);
	}
	@DeleteMapping("/deleteInventory/{id}")
	public void deleteInventory(@PathVariable String id){
		inventoryService.deleteInventory(id);
	}
	
	@PostMapping("/addExtraFieldName")
	public void addExtraFieldName(@RequestBody ExtraFieldNameDTO extraFieldNameDTO) throws Exception {
		inventoryService.addInventoryExtraField(extraFieldNameDTO);
	}
	@GetMapping("/getExtraFieldName/{companyId}")
	public List<ExtraFieldNameDTO> getExtraFieldName(@PathVariable String companyId){
		System.out.println("----------my company------------->"+companyId);
		return inventoryService.getInventoryExtraField(companyId);
	}
	@DeleteMapping("/deleteExtraFieldName/{id}")
	public void deleteExtraFieldName(@PathVariable String id) {
		System.out.println("-----------------------api------------------------>"+id);
		inventoryService.deleteInventoryExtraField(id);
	}
	@PostMapping("/mandatoryFields")
	public void mandatoryFields(@RequestBody MandatoryFields mandatoryFields){
		inventoryService.updateMandatoryFields(mandatoryFields);
	}
	@PostMapping("/showFields")
	public void showFields(@RequestBody ShowFields showFields){
		inventoryService.updateShowFields(showFields);
	}
	@GetMapping("/getMandatoryFields/{name}/{companyId}")
	public ResponseEntity<MandatoryFields> getMandatoryFields(@PathVariable String name,@PathVariable String companyId){
		System.out.println("============================>"+name+companyId);
		MandatoryFields mandatoryFields=inventoryService.getMandatoryFields(name,companyId);
		return ResponseEntity.ok(mandatoryFields);
	}
	@GetMapping("/getShowFields/{name}/{companyId}")
	public ResponseEntity<ShowFields> getShowFields(@PathVariable String name,@PathVariable String companyId){
		ShowFields showFields=inventoryService.getShowFields(name,companyId);
		return ResponseEntity.ok(showFields);
	}
	@GetMapping("/getAllMandatoryFields/{companyId}")
	public ResponseEntity<List<MandatoryFields>> getAllMandatoryFields(@PathVariable String companyId){
		List<MandatoryFields> mandatoryFieldsList=inventoryService.getAllMandatoryFields(companyId);
		return ResponseEntity.ok(mandatoryFieldsList);
	}
	@GetMapping("/getAllShowFields/{companyId}")
	public ResponseEntity<List<ShowFields>> getAllShowFields(@PathVariable String companyId){
		List<ShowFields> showFieldsList=inventoryService.getAllShowFields(companyId);
		return ResponseEntity.ok(showFieldsList);
	}
	@DeleteMapping("/deleteShowAndMandatoryField/{name}/{companyId}")
	public void showFields(@PathVariable String name,@PathVariable String companyId){
		inventoryService.deleteShowAndMandatoryFields(companyId, name);
	}
	@GetMapping("/getExtraFieldNameValue/{companyId}")
	public Map<String, Map<String,String>> getExtraFieldNameValue(@PathVariable String companyId){
		return inventoryService.getExtraFieldList(companyId);
	}
	@PostMapping("/addfields")
	public void addNewFields(@RequestBody ExtraFieldsDTO extraFieldsDTO) throws Exception {
		inventoryService.addExtraFields(extraFieldsDTO);
	}
	@GetMapping("/getExtraFields/{id}")
	public List<ExtraFieldsDTO> getExtraFields(@PathVariable String id){
		return inventoryService.getExtraFields(id);
	}
	@DeleteMapping("/deleteExtraFields/{id}")
	public void deleteExtraField(@PathVariable String id) throws Exception {
		inventoryService.deleteExtraFields(id);
	}
	@DeleteMapping("/deleteInventoryExtraFields/{id}")
	public void deleteInventoryExtraFields(@PathVariable String id) throws Exception {
		inventoryService.deleteExtraFieldByInventory(id);
	}
	@GetMapping("/allInventoryWithExtraFields/{companyId}")
	public List<String> getAllInventoryList(@PathVariable String companyId){
		return inventoryService.inventoryAllData( companyId);
	}
	
	@PostMapping("/import/{companyId}/{email}")
	public void importFile(@RequestParam("file") MultipartFile file,@RequestParam("columnMappings") String columnMappings,@PathVariable String companyId,@PathVariable String email) throws Exception {
//		System.out.println("------||---------------------------------------/////////////////////////////////////------->"+columnMappings);
	
        
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

                
                if(key.equals("")==false&&val.equals("")==false) {
                	
                	columnMap.put(key, val);
                	System.out.println("---->"+key+" "+val+" "+columnMap.size());
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
	 columnMap.forEach((x,y)->{
		 System.out.println(x+" "+y);
	 });
	 try (InputStream inputStream = file.getInputStream();
	         BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
		  if(reader.lines().count()>5001) {
			  throw new ImportFileRowException("Import File cannot import more than 5000 rows");
		  }
	       
	    } catch (IOException e) {
	        // Handle IOException
	        e.printStackTrace();
	       
	    }
		List<InventoryDTO> assetList=new ArrayList<InventoryDTO>(); 
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
     	        cell1.setCellValue("Row "+(int)(ind+1));
     	       InventoryDTO inventoryDTO = new InventoryDTO();
     	      inventoryDTO.setCompanyId(companyId);
	            int errorFlag=0;
	            String errorDesc="";

	            for (int j = 0; j < row.length; j++) {
	            	
	                String field = headerMap.get(j);
	                
	          
	                System.out.println("--------------> "+field+" "+field.length());


	                if(errorFlag==1) {
	                	break;
	                }
	                
	                System.out.println("--------------> "+field);
	                
	                switch (columnMap.get(field).toLowerCase()) {
	                
	                

	                case "partid":
	                	inventoryDTO.setPartId(row[j]);
	                    break;
	                    
	                case "partname":
	                	inventoryDTO.setPartName(row[j]);
	                    break;
	                case "price":
	                	try {
	                		inventoryDTO.setPrice( Float.parseFloat(row[j]));
		                	}
		                	catch(Exception e){
		                		errorFlag=1;
		                		System.out.println("Error in Price"+e);
		                		if(errorDesc.length()>0) {
		                			errorDesc+=", ";
		                		}
		                		errorDesc+="ERROR WHILE ADDING IN PRICE";
		                	}
	                	
	                    break;
	                case "cost":
	                	
	                	try {
	                		inventoryDTO.setCost( Float.parseFloat(row[j]));
		                	}
		                	catch(Exception e){
		                		System.out.println("Error in Cost"+e);
		                		errorFlag=1;
		                		if(errorDesc.length()>0) {
		                			errorDesc+=", ";
		                		}
		                		errorDesc+="ERROR WHILE ADDING IN COST";
		                	}
	                	
	                case "category":
	                	inventoryDTO.setCategory(row[j]);
	                    break;
	                case "quantity":
               	
	                	
	                	try {
	                		inventoryDTO.setQuantity( Integer.parseInt(row[j]));
		                	}
		                	catch(Exception e){
		                		System.out.println("Error in Cost"+e);
		                		errorFlag=1;
		                		if(errorDesc.length()>0) {
		                			errorDesc+=", ";
		                		}
		                		errorDesc+="ERROR WHILE ADDING IN COST";
		                	}
	                    break;
	         

	                

	            }

	                
	            
	            }
//	            System.out.println("///'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/  reached errorFlag" +errorFlag);
	            if(errorFlag==0) {
//	            	System.out.println("///'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/'/");
	            	Inventory mynewInventory=inventoryService.addInventory(inventoryDTO);
		            
		            //-------------------------------------------
		            for (int j = 0; j < row.length; j++) {
		                String field = headerMap.get(j);
//		                System.out.println("-------|||||-------> "+columnMap.get(field).toLowerCase());
	
		                
		                
		                
		                
		               
		                
		                String value=row[j];
		                
		               List<ExtraFieldName> listExtraFieldName= extraFieldNameRepository.findByCompanyId(companyId);
	
//		               listExtraFieldName.stream().forEach((x)->{
//		            	   if(columnMap.get(field).toLowerCase().equals(x.getName().toLowerCase())) {
//		            		   ExtraFields extraFieldsDTO=new ExtraFields(); 
//		            		   extraFieldsDTO.setInventoryId(mynewInventory.getId());
//		            		   extraFieldsDTO.setName(x.getName());
//		            		   extraFieldsDTO.setType(x.getType());
//		            		   extraFieldsDTO.setValue(value);
//		            		   extraFieldsDTO.setCompanyId(companyId);
//		            		   extraFieldsRepository.save(extraFieldsDTO);
//		            	   }
//		               });
		               for(int i=0;i<listExtraFieldName.size();i++) {
		            	   if(columnMap.get(field).toLowerCase().equals(listExtraFieldName.get(i).getName().toLowerCase())) {
		            		   System.out.println("-----------------working ---->"+listExtraFieldName.get(i).getType());
		            		   ExtraFields extraFieldsDTO=new ExtraFields(); 
		            		   extraFieldsDTO.setInventoryId(mynewInventory.getId());
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
		            				   System.out.println("ERROR WHILE ADDING EXTRA IN"+ listExtraFieldName.get(i).getName()+" for row->"+ind);
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
		            		   if(errorFlag==0) {
		            		   extraFieldsRepository.save(extraFieldsDTO);
		            		   }
		            		   else {
		            			   Inventory myInventory=modelMapper.map(mynewInventory, Inventory.class);
		            			   inventoryRepository.delete(myInventory);
		            			   
		            		   }
		            	   }
		               }
	
		            
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
	        	 try (FileOutputStream fileOut = new FileOutputStream("InventoryReport.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, We have attached import result below");
	            helper.addAttachment("InventoryAttachment.xlsx", new File("InventoryReport.xlsx"));

	            emailSender.send(message);
	        }
	        if(excelIndex==0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("InventoryReport.xlsx")) {
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
	public void updateAssetWithFile(@RequestParam("file") MultipartFile file,@RequestParam("columnMappings") String columnMappings,@PathVariable String companyId,@PathVariable String email) throws CsvValidationException, JsonParseException, IOException, MessagingException, ImportFileRowException {
		
		
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
		List<InventoryDTO> inventoryList=new ArrayList<InventoryDTO>(); 
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
	     	        cell1.setCellValue("Row "+(int)(ind+1));
	        	int errorFlag=0;
	        	  String errorDesc="";
//	            AssetsDTO assetsDTO = new AssetsDTO();
//	            assetsDTO.setCompanyId(companyId);
	        	  Inventory inventory=new Inventory();
	            for (int j = 0; j < row.length; j++) {
	            	if(j>0&&inventory==null) {
	            		break;
	            	}
	                String field = headerMap.get(j);
//	                System.out.println("--------------> "+j+" " + row[j]);
	                System.out.print("-------|||||-------> "+columnMap.get(field).toLowerCase());

	                
	                
	                
	               
	                switch ( columnMap.get(field).toLowerCase()) {
	                case "inventoryid":
	                    String inventoryIdValue = row[j].trim();
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
	                    inventory=inventoryRepository.findByInventoryIdAndCompanyId(Integer.parseInt(inventoryIdValue),companyId);
//	                    System.out.println("------------------/////////----"+assets.getId());
//	                    assetsDTO.setId(assets.getId());
//	                    System.out.println("------------------/////////----"+assetsDTO.getId());
	                    if(inventory==null) {
	                    	errorFlag=1;
	                    	 errorDesc+="ERROR WHILE UPDATING IN INVENTORYID";
	                    }
	                    break;
	                

	                case "partid":
	                	inventory.setPartId(row[j]);
	                    break;
	                    
	                case "partname":
	                	inventory.setPartName(row[j]);
	                    break;
	                case "price":
	                	try {
	                	inventory.setPrice( Float.parseFloat(row[j]));
	                	}
	                	catch(Exception e){
	                		System.out.println("Error in Price"+e);
	                		if(errorDesc.length()>0) {
	                			errorDesc+=", ";
	                		}
	                		errorDesc+="ERROR WHILE ADDING IN PRICE";
	                		errorFlag=1;
	                	}
	                    break;
	                case "cost":
	                	try {
	                		inventory.setCost( Float.parseFloat(row[j]));
		                	}
		                	catch(Exception e){
		                		System.out.println("Error in Cost"+e);
		                		if(errorDesc.length()>0) {
		                			errorDesc+=", ";
		                		}
		                		errorDesc+="ERROR WHILE ADDING IN COST";
		                		errorFlag=1;
		                	}
	                	
	                case "category":
	                	inventory.setCategory(row[j]);
	                    break;
	                case "quantity":
               	
	                	
	                	try {
	                		inventory.setQuantity( Integer.parseInt(row[j]));
		                	}
		                	catch(Exception e){
		                		System.out.println("Error in Cost"+e);
		                		if(errorDesc.length()>0) {
		                			errorDesc+=", ";
		                		}
		                		errorDesc+="ERROR WHILE ADDING IN COST";
		                		errorFlag=1;
		                	}
	                    break;
	         

	                

	            }
	                if(errorFlag==0) {
	                String value=row[j];
	                
		               List<ExtraFieldName> listExtraFieldName= extraFieldNameRepository.findByCompanyId(companyId);
		               String id=inventory.getId();
//		               System.out.println("--------id------>"+id);
		               for(int i=0;i<listExtraFieldName.size();i++) {
		            	   if(columnMap.get(field).toLowerCase().equals(listExtraFieldName.get(i).getName().toLowerCase())) {
		            		   ExtraFields extraFieldsDTO=new ExtraFields(); 
		            		  ExtraFields extraFieldsOptional=extraFieldsRepository.findByNameAndInventoryId(listExtraFieldName.get(i).getName(), id);
		            		   System.out.println("-----------------working ---->"+listExtraFieldName.get(i).getType());
		            		   if(extraFieldsOptional!=null) {
			            		   extraFieldsDTO.setId(extraFieldsOptional.getId());
			            		   extraFieldsDTO.setInventoryId(id);
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
			            		   extraFieldsDTO.setCompanyId(companyId);
			            		   extraFieldsRepository.save(extraFieldsDTO);
			            		   }
			            		   else {
			            			   extraFieldsDTO.setInventoryId(id);
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
				            		   extraFieldsDTO.setCompanyId(companyId);
				            		   extraFieldsRepository.save(extraFieldsDTO);
			            		   }
		            		  
		            		 
		            		   
		            		   if(errorFlag==0) {
		            		   extraFieldsRepository.save(extraFieldsDTO);
		            		   }
		            	   }
		               } 
	                }
	            
	            }
	            
	            if(errorFlag==0) {
	            	System.out.println("saving inventory"+inventory.getId());
//	            InventoryDTO inventoryDTO = modelMapper.map(inventory, InventoryDTO.class);
	            inventoryRepository.save(inventory);
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
	        	 try (FileOutputStream fileOut = new FileOutputStream("InventoryReport.xlsx")) {
	    	            workbook.write(fileOut);
	    	        }
	        	 workbook.close();
	        	MimeMessage message = emailSender.createMimeMessage();
	            MimeMessageHelper helper = new MimeMessageHelper(message, true);

	            helper.setTo(email);
	            helper.setSubject("Import Report from AssetYug");
	            helper.setText("Hey, We have attached import result below");
	            helper.addAttachment("InventoryAttachment.xlsx", new File("InventoryReport.xlsx"));

	            emailSender.send(message);
	        }
	        if(excelIndex==0) {
	        	 try (FileOutputStream fileOut = new FileOutputStream("InventoryReport.xlsx")) {
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


	    
//	        assetsService.importExcel(assetList);

	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	
}
