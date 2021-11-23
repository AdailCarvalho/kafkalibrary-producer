package com.adaverso.kafkalibrary.producer.controller.advice;

import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class LibraryEventControllerAdvice {
	
	private static final Logger log = LoggerFactory.getLogger(LibraryEventControllerAdvice.class);

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<?> handleRequestBody(MethodArgumentNotValidException ex) {
		List<FieldError> fieldErrorList = ex.getBindingResult().getFieldErrors();
		String errorMessage = fieldErrorList.stream()
				 .map(fieldError -> fieldError.getField() + " - " + fieldError.getDefaultMessage())
				 .sorted()
				 .collect(Collectors.joining(", "));
		
		log.info("Error: {}", errorMessage);
		return new ResponseEntity<>(errorMessage, HttpStatus.BAD_REQUEST);
	}
}
