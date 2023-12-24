package com.quantumai.customer.utility;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.quantumai.customer.exception.ErrorInfo;
import com.quantumai.customer.exception.NoSubscriptionError;
import com.quantumai.customer.exception.UserAlreadyPresentException;

@RestControllerAdvice
public class ExceptionControllerAdvice {
	@ExceptionHandler(UserAlreadyPresentException.class)
	public ResponseEntity<ErrorInfo>UserAlreadyPresentException(UserAlreadyPresentException exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("Email Already Registered");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
		
	}
	@ExceptionHandler(NoSubscriptionError.class)
	public ResponseEntity<ErrorInfo>NoSubscriptionErrorException(NoSubscriptionError exception){
		ErrorInfo errorInfo=new ErrorInfo();
		errorInfo.setErrorMessage("No Subscription");
		errorInfo.setErrorCode(HttpStatus.BAD_REQUEST.value());
		return new ResponseEntity<>(errorInfo,HttpStatus.BAD_REQUEST);
	}
}
