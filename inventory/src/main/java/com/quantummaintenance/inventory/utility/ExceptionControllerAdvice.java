package com.quantummaintenance.inventory.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import  com.quantummaintenance.inventory.exception.*;




@RestControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(ImportFileRowException.class)
	public ResponseEntity<ErrorInfo>UserAlreadyPresentException(ImportFileRowException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Import File cannot import more than 5000 rows");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	
}
