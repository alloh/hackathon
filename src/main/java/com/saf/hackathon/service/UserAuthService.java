package com.saf.hackathon.service;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.saf.hackathon.config.security.User;
import com.saf.hackathon.model.ApplicationUser;
import com.saf.hackathon.pojo.LoginObj;


/**
 * This interface is used for all user authentication services - these functions
 * will be shared by all the clients of this product
 *
 * @author alagat
 *
 */
public interface UserAuthService {

	/**
	 * This function is used to change user's passwords
	 *
	 * @param userName
	 * @param newPassword
	 * @return
	 */
	Boolean changePassword(final String userName, final String newPassword);

	/**
	 * checks if the password given by the user corresponds to the pasword in
	 * the database
	 *
	 * @param databasePassword
	 * @param userProvidedPassword
	 * @return
	 * @throws Exception
	 */
	public boolean isCorrectPassword(String dbPassword, String userProvidedPassword) throws Exception;

	/**
	 * used to load User by userName
	 *
	 * @param username
	 *            the username to get user;
	 * @return
	 * @throws UsernameNotFoundException
	 */
	UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException;

	/**
	 * refresh the user Token
	 *
	 * @param token
	 * @param refreshedToken
	 * @param refreshTime
	 */
	void refreshAuthLoginToken(String token, String refreshedToken, String refreshTime);

	/**
	 * Used to check if the credentials given are valid
	 *
	 * @param loginPOJO
	 * @return
	 * @throws Exception
	 */
	User isValidUser(LoginObj loginPOJO) throws Exception;

	/**
	 * Used to generate password reset tokens
	 *
	 * @param selfServiceUser
	 * @return
	 */
	//PasswordResetToken generateToken(AppUser selfServiceUser);

	/**
	 * creates UserLoginToken
	 *
	 * @param user
	 * @param token
	 * @param refreshTime
	 * @param createdTime
	 */
	void createAuthLoginToken(ApplicationUser user, String token, String refreshTime, String createdTime);

}
