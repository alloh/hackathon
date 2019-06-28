package com.saf.hackathon.model.repositories;

import org.springframework.data.repository.CrudRepository;

import com.saf.hackathon.model.ApplicationUser;

public interface ApplicationUserRepository extends CrudRepository<ApplicationUser, String> {
	ApplicationUser findByUsername(String userName);
}
