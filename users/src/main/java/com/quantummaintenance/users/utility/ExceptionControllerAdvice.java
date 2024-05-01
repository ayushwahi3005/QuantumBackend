package com.quantummaintenance.users.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quantummaintenance.users.exception.*;
import com.quantummaintenance.users.exception.UserException;
import com.quantummaintenance.users.exception.UserCannotDeletedException;



@RestControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(UserException.class)
	public ResponseEntity<ErrorInfo>UserAlreadyInvitedException(UserException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Already Invited");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(UserCannotDeletedException.class)
	public ResponseEntity<ErrorInfo>UserCannotDeleted(UserCannotDeletedException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("WorkOrder is assigned to this user. Cannot delete the user!!");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(TheMailException.class)
	public ResponseEntity<ErrorInfo>TheMailException(TheMailException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Error sending mail.Connection Timeout !!");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	
}
