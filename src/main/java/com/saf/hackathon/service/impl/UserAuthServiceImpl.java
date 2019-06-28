package com.saf.hackathon.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.saf.hackathon.config.security.User;
import com.saf.hackathon.exception.ExpectationFailedException;
import com.saf.hackathon.exception.ObjectNotFoundException;
import com.saf.hackathon.model.ApplicationUser;
import com.saf.hackathon.model.AuthLoginToken;
import com.saf.hackathon.model.repositories.ApplicationUserRepository;
import com.saf.hackathon.model.repositories.AuthLoginTokenRepository;
import com.saf.hackathon.pojo.LoginObj;
import com.saf.hackathon.service.UserAuthService;



/**
 * This class will be used for all user authentication services
 *
 * @author alagat
 *
 */
@Service
public class UserAuthServiceImpl implements UserAuthService {
	@Value("${login.error}")
	private String LoginError;
	private static final Logger logger = LoggerFactory.getLogger(UserAuthServiceImpl.class);
	@Autowired
	private ApplicationUserRepository selfServiceUserRepository;
	@Autowired
	private PasswordEncoder encoder;

	@Autowired
	private Environment env;

	@Autowired
	private AuthLoginTokenRepository authLoginTokenRepository;
	@Value("${saf.token.expiration}")
	private String tokenExpiryTime;
	@Value("${user.notexist.error}")
	private String userNotExistError;

	@Override
	public User isValidUser(LoginObj loginPOJO) throws Exception {
		final User user = (User) loadUserByUserName(loginPOJO.getUsername());
		final ApplicationUser selfServiceUser = user.getSelfServiceUser();
		if (selfServiceUser == null) {
			throw new ObjectNotFoundException(env.getProperty("login.error"));
		}
		final String password = selfServiceUser.getPassword();
		if (isCorrectPassword(password, loginPOJO.getPassword())) {
			return user;
		}
		throw new ExpectationFailedException(LoginError);
	}

	@Override
	public UserDetails loadUserByUserName(String userName) throws UsernameNotFoundException {
		UserDetails userDetails = null;
		final List<GrantedAuthority> authorities = new ArrayList<>();
		try {
			userDetails = loadClient(userName, authorities);
			if (userDetails == null) {
				throw new UsernameNotFoundException("User " + userName + " not found");
			}
		} catch (final Exception e) {
			throw new RuntimeException(e);
		}
		return userDetails;
	}

	@Override
	public boolean isCorrectPassword(String dbPassword, String userProvidedPassword) throws Exception {
		return encoder.matches(userProvidedPassword, dbPassword);
	}

	/**
	 * Loads a client from the database
	 *
	 * @param username
	 * @param authorities
	 * @return
	 * @throws Exception
	 */
	private UserDetails loadClient(final String userName, final List<GrantedAuthority> authorities) throws Exception {
		logger.debug("Load Client");
		User userDetails = null;

			final ApplicationUser selfServiceUser = selfServiceUserRepository.findByUserName(userName);
			if (selfServiceUser != null) {
				userDetails = new User(selfServiceUser, selfServiceUser.getUsername(), selfServiceUser.getPassword(),
						authorities);
			}

		return userDetails;
	}


	@Override
	public Boolean changePassword(String userName, String newPassword) {
		final ApplicationUser selfServiceUser = selfServiceUserRepository.findByUserName(userName);
		if (selfServiceUser != null) {
			selfServiceUser.setPassword(encoder.encode(newPassword));
			selfServiceUserRepository.save(selfServiceUser);
			return true;
		}
		return false;
	}

	@Override
	public void createAuthLoginToken(ApplicationUser user, String token, String refreshTime, String createdTime) {
		AuthLoginToken authLoginToken = authLoginTokenRepository.findAuthLoginTokenByUserid(user.getId());
		if (authLoginToken == null) {
			authLoginToken = new AuthLoginToken();
			authLoginToken.setTokenId(createdTime);
		}
		authLoginToken.setToken(token);
		authLoginToken.setRefreshtime(refreshTime);
		authLoginTokenRepository.save(authLoginToken);
	}

	@Override
	public void refreshAuthLoginToken(String token, String refreshedToken, String refreshTime) {
		final AuthLoginToken authLoginToken = authLoginTokenRepository.findAuthLoginTokenByToken(token);
		authLoginToken.setTokenId(String.valueOf(System.currentTimeMillis()));
		authLoginToken.setToken(refreshedToken);
		authLoginToken.setRefreshtime(refreshTime);
		authLoginTokenRepository.save(authLoginToken);
	}
}
