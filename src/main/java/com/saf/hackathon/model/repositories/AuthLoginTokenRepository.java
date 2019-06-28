package com.saf.hackathon.model.repositories;

import org.springframework.data.repository.CrudRepository;

import com.saf.hackathon.model.AuthLoginToken;

public interface AuthLoginTokenRepository extends CrudRepository<AuthLoginToken, String> {
	/**
	 * find AuthLoginToken by the parameters
	 *
	 * @param token
	 * @return
	 */
	AuthLoginToken findAuthLoginTokenByToken(String token);

	/**
	 * find AuthLoginToken by the parameters
	 *
	 * @param userName
	 * @return
	 */
	AuthLoginToken findAuthLoginTokenByUserid(String userName);
}

