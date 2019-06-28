package com.saf.hackathon.facade.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;

import com.saf.hackathon.config.security.User;
import com.saf.hackathon.exception.PreconditionFailedException;
import com.saf.hackathon.facade.UserFacade;
import com.saf.hackathon.model.ApplicationUser;
import com.saf.hackathon.model.repositories.ApplicationUserRepository;
import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.LoginObj;
import com.saf.hackathon.pojo.AuthTokenObj;
import com.saf.hackathon.pojo.UserObj;
import com.saf.hackathon.service.ApplicationUserService;
import com.saf.hackathon.service.UserAuthService;
import com.saf.hackathon.util.AuthTokenUtil;
import com.saf.hackathon.util.annotation.Facade;


/**
 *  Implementation of user related services
 *  
 * @author user
 *
 */
@Facade
public class UserFacadeImpl implements UserFacade{
 @Autowired
 private ApplicationUserService<ApplicationUser> userService;
 @Autowired
 private ApplicationUserRepository applicationUserRepository;
 @Value("${user.exists}")
 private String userExists;
 @Autowired
 private AuthTokenUtil tokenUtils;
 @Autowired
 private UserAuthService userAuthService;
 //autowire password encoder
	@Override
	public ResponseEntity<Add> createUser(UserObj filter) throws Exception {
		//check if the username exists
		ApplicationUser exists = applicationUserRepository.findByUsername(filter.getUserName());
		if(exists!=null){
			throw new PreconditionFailedException(userExists);
		}
		ApplicationUser user = userService.prepareAppUserForInsert(filter);
		applicationUserRepository.save(user);
		Add add = new Add();
		add.setAdd(true);
		return ResponseEntity.ok(add);
	}

	@Override
	public ResponseEntity<AuthTokenObj> loginUser(LoginObj filter) throws Exception {
		final User user = userAuthService.isValidUser(filter);
		final String token = tokenUtils.generateToken(user, null);
		AuthTokenObj authTokenObj = new AuthTokenObj();
		authTokenObj.setToken(token);
		return ResponseEntity.ok(authTokenObj);
	}

}
