package com.saf.hackathon.service.impl;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saf.hackathon.model.ApplicationUser;
import com.saf.hackathon.pojo.UserObj;
import com.saf.hackathon.service.ApplicationUserService;
/**
 * Handles all user related dao
 * @author alagat
 *
 */
@Service
public class ApplicationUserServiceImpl implements ApplicationUserService<ApplicationUser> {
	@Autowired
	private PasswordEncoder encoder;

	@Override
	public ApplicationUser prepareAppUserForInsert(UserObj filter) throws Exception {
		ApplicationUser user = new ApplicationUser();
		user.setFirstname(filter.getFirstName());
		user.setLastname(filter.getLastName());
		user.setPassword(encoder.encode(filter.getPassword()));
		user.setUsername(filter.getUserName());
		user.setId(UUID.randomUUID().toString());
		return user;
	}

}