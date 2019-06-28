package com.saf.hackathon.exception.controller;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import com.saf.hackathon.exception.ObjectNotFoundException;
import com.saf.hackathon.exception.PreconditionFailedException;
import com.saf.hackathon.pojo.APIError;
import com.saf.hackathon.service.UtilService;



@RestControllerAdvice
public class ControllerAdvice {
	private final org.slf4j.Logger logger = LoggerFactory.getLogger(ControllerAdvice.class);
	@Value("${object.not.found.error}")
	private String objectNotFound;
	@Autowired
	private UtilService utilService;
	@Autowired
	private Environment env;
    @Value("${generic.error}")
    private String genericError;
	@ResponseBody
	@ExceptionHandler(ObjectNotFoundException.class)
	@ResponseStatus(HttpStatus.NOT_FOUND)
	ResponseEntity<APIError> objectNotFoundExceptionHandler(ObjectNotFoundException ex) {
		logger.error(ex.getMessage());
		ex.printStackTrace();
		// for debugging purposes
		final APIError apiError = new APIError();
		apiError.setMsgDeveloper(
				ex.getMessage() == null || ex.getMessage().isEmpty() ? utilService.getStackTrace(ex) : ex.getMessage());
		apiError.setMsgUser(ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : objectNotFound);
		apiError.setCode(String.valueOf(HttpStatus.EXPECTATION_FAILED));
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(apiError);

	}
	
	@ResponseBody
	@ExceptionHandler(PreconditionFailedException.class)
	@ResponseStatus(HttpStatus.EXPECTATION_FAILED)
	ResponseEntity<APIError> preconditionFailedExceptionHandler(PreconditionFailedException ex) {
		logger.error(ex.getMessage());
		ex.printStackTrace();
		// for debugging purposes
		final APIError apiError = new APIError();
		apiError.setMsgDeveloper(
				ex.getMessage() == null || ex.getMessage().isEmpty() ? utilService.getStackTrace(ex) : ex.getMessage());
		apiError.setMsgUser(ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage() : objectNotFound);
		apiError.setCode(String.valueOf(HttpStatus.EXPECTATION_FAILED));
		return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(apiError);

	}
	
	@ResponseBody
	@ExceptionHandler(Exception.class)
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	ResponseEntity<APIError> exceptionHandler(Exception ex) {
		logger.error(ex.getMessage()); ex.printStackTrace();
		// for debugging purposes
		final APIError apiError = new APIError();
		apiError.setMsgDeveloper(
				ex.getMessage() == null || ex.getMessage().isEmpty() ? utilService.getStackTrace(ex) : ex.getMessage());
		apiError.setMsgUser(env.getProperty("road.rescue.error"));
		apiError.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(apiError);

	}
}
