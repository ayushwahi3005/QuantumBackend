package com.quantummaintenance.users.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quantummaintenance.users.exception.ErrorInfo;
import com.quantummaintenance.users.exception.UserException;



@RestControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorInfo>UserAlreadyInvitedException(UserException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Already Invited");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	
}
