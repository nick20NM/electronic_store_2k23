package com.alpha.www.ElectronicStore.exception;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.alpha.www.ElectronicStore.dto.ApiResponseMessage;

@RestControllerAdvice
public class GlobalExceptionHandler {
	
	private Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

	// ResourceNotFoundException handler
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<ApiResponseMessage> resourceNotFoundExceptionHandler(ResourceNotFoundException e){
		
		logger.info("from resourceNotFoundExceptionHandler method");
		
		ApiResponseMessage response = ApiResponseMessage
			.builder()
			.message(e.getMessage())
			.success(true)
			.status(HttpStatus.NOT_FOUND)
			.build();
		return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
	}
	
	// MethodArgumentNotValidException handler
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, Object>> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException e){
		
		List<ObjectError> allErrors = e.getBindingResult().getAllErrors();
		
		Map<String, Object> response = new HashMap<>();
		
		allErrors.stream().forEach(objectError -> {
			String message = objectError.getDefaultMessage();
			String field = ((FieldError)objectError).getField();
			response.put(field, message);
		});
		
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
		
	}
	
	// BadApiRequestException handler
	@ExceptionHandler(BadApiRequestException.class)
	public ResponseEntity<ApiResponseMessage> badApiRequestExceptionHandler(BadApiRequestException e){
		
		logger.info("from badApiRequestExceptionHandler method");
		
		ApiResponseMessage response = ApiResponseMessage
			.builder()
			.message(e.getMessage())
			.success(true)
			.status(HttpStatus.BAD_REQUEST)
			.build();
		return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
	}
}
