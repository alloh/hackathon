package com.saf.hackathon.facade;

import org.springframework.http.ResponseEntity;

import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.LoginObj;
import com.saf.hackathon.pojo.AuthTokenObj;
import com.saf.hackathon.pojo.UserObj;

/**
 * Groups all user related services definitions
 * 
 * @author alagat
 *
 */
public interface UserFacade {
	ResponseEntity<Add> createUser(UserObj filter) throws Exception;
	
	ResponseEntity<AuthTokenObj> loginUser (LoginObj filter) throws Exception;
}
