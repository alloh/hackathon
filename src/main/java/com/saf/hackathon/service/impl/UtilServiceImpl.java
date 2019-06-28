package com.saf.hackathon.service.impl;

import java.security.SecureRandom;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Random;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.saf.hackathon.exception.ExpectationFailedException;
import com.saf.hackathon.service.UtilService;


@Service
public class UtilServiceImpl implements UtilService {
	@Value("${source.header.value}")
	private String sourceApiHeader;
	@Value("${token.header}")
	private String authorisationHeader;
	@Value("${context.not.found}")
	private String contextNotFoundError;

	@Override
	public String getStackTrace(Exception e) {
		final StringBuilder sb = new StringBuilder();
		int counter = 0;
		for (final StackTraceElement element : e.getStackTrace()) {
			sb.append(element.toString());
			sb.append(" - ");

			if (counter == 20) {
				break;
			}
			counter++;
		}
		return sb.toString();
	}

	@Override
	public byte[] generateRandomNumberByte(int bytes) throws Exception {
		final Random ranGen = SecureRandom.getInstanceStrong();
		final byte[] randomKey = new byte[bytes]; // 1 byte = 8 bits
		ranGen.nextBytes(randomKey);
		return randomKey;
	}

	@Override
	public HttpHeaders addCORSHeaders() {
		final HttpHeaders headers = new HttpHeaders();
		headers.set("Access-Control-Allow-Origin", "*");
		headers.set("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		headers.set("Access-Control-Max-Age", "3600");
		headers.set("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, " + sourceApiHeader + ", " + authorisationHeader);
		return headers;
	}

	@Override
	public HttpServletRequest getCurrentRequest() throws Exception {
		final RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
		if (requestAttributes == null) {
			throw new ExpectationFailedException(contextNotFoundError);
		}
		return ((ServletRequestAttributes) requestAttributes).getRequest();
	}

	@Override
	public HttpServletResponse addCORSHeaders(HttpServletResponse response) {
		response.setHeader("Access-Control-Allow-Origin", "*");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, " + sourceApiHeader + ", " + authorisationHeader);
		return response;
	}
	
	@Override
	public Date getCurrentDate() throws Exception {
		Date now = new Date();
		return now;
	}

	@Override
	public Timestamp getCurrentTimeStamp() throws Exception {
		Date instant = new Date();
		Timestamp timestamp = new Timestamp(instant.getTime());
		return timestamp;
	}

}
