package com.quantummaintenance.workorder.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quantummaintenance.workorder.exception.ErrorInfo;
import com.quantummaintenance.workorder.exception.ExtraFieldAlreadyPresentException;



@RestControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(ExtraFieldAlreadyPresentException.class)
	public ResponseEntity<ErrorInfo>UserAlreadyPresentException(ExtraFieldAlreadyPresentException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Field Already Present");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	
}
