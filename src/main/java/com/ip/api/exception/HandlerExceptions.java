package com.ip.api.exception;


import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class HandlerExceptions {

	@ExceptionHandler(IpInfoServiceException.class)
	public ResponseEntity<String> getQuoteProcessImplException(IpInfoServiceException e) {
		log.error("Error: ", e);
		return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
	}

}
