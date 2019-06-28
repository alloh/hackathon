package com.saf.hackathon.service;

import java.sql.Timestamp;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpHeaders;

/**
 * 
 * Contains common functionalities across different modules
 * @author alagat 
 */
public interface UtilService {
	String getStackTrace(Exception e);

	byte[] generateRandomNumberByte(int bytes) throws Exception;

	HttpHeaders addCORSHeaders();

	HttpServletRequest getCurrentRequest() throws Exception;

	HttpServletResponse addCORSHeaders(HttpServletResponse response);

	Date getCurrentDate() throws Exception;

	Timestamp getCurrentTimeStamp() throws Exception;
}
