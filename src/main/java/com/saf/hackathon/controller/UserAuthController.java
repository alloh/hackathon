package com.saf.hackathon.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.saf.hackathon.facade.UserFacade;
import com.saf.hackathon.facade.UtilityFacade;
import com.saf.hackathon.pojo.Add;
import com.saf.hackathon.pojo.LoginObj;
import com.saf.hackathon.pojo.AuthTokenObj;
import com.saf.hackathon.pojo.UserObj;

@RestController
@RequestMapping("/saf/user")
public class UserAuthController {

	@Autowired
	private UtilityFacade utilityFacade;
	
	@Autowired
	private UserFacade userFacade;
	
	@RequestMapping(value = "/filter", method = RequestMethod.GET)
	public ResponseEntity<UserObj> filter() throws Exception {
		
		return new ResponseEntity<>(new UserObj(), HttpStatus.OK);
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<Add> registerUser(@Valid @RequestBody UserObj filter,
			BindingResult bindingResult) throws Exception {
		utilityFacade.validateRequest(bindingResult);
		return userFacade.createUser(filter);
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseEntity<AuthTokenObj> userLogin(@Valid @RequestBody LoginObj filter,
			BindingResult bindingResult) throws Exception {
		utilityFacade.validateRequest(bindingResult);
		return userFacade.loginUser(filter);
	}
}
