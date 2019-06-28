package com.saf.hackathon.util;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mobile.device.Device;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import com.saf.hackathon.config.security.User;
import com.saf.hackathon.exception.ExpectationFailedException;
import com.saf.hackathon.exception.UnAuthorisedUserException;
import com.saf.hackathon.model.ApplicationUser;
import com.saf.hackathon.model.AuthLoginToken;
import com.saf.hackathon.model.repositories.ApplicationUserRepository;
import com.saf.hackathon.model.repositories.AuthLoginTokenRepository;
import com.saf.hackathon.service.UserAuthService;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

/**
 * Creates Authentication Access Token
 *
 * @author alagat
 *
 */
@Component
public class AuthTokenUtil {
	@Autowired
	private AuthLoginTokenRepository authLoginTokenRepository;
	@Autowired
    private UserAuthService userService;
	@Autowired
	private ApplicationUserRepository appServiceUserRepository;

	private final Logger log = LoggerFactory.getLogger(AuthTokenUtil.class);

	private final static String AUDIENCE_UNKNOWN = "unknown";
	private final static String AUDIENCE_WEB = "web";
	private final static String AUDIENCE_MOBILE = "mobile";
	private final static String AUDIENCE_TABLET = "tablet";
	private final static String created = "created";
	private String secret;
	@Value("${invalid.token}")
	private String invalidToken;

	@Value("${saf.token.expiration}")
	private Long expiration;

	public ApplicationUser getUserFromToken(String token) throws Exception {
		if (token != null && !token.isEmpty()) {
			final AuthLoginToken userLoginToken = authLoginTokenRepository.findAuthLoginTokenByToken(token);
			final UserDetails userDetails = userLoginToken != null ? userService.loadUserByUserName(
					appServiceUserRepository.findById(userLoginToken.getUserid()).get().getUsername()) : null;
					if (userDetails != null && validateToken(token, userDetails)) {
						return appServiceUserRepository.findById(userLoginToken.getUserid()).get();
					} else {
						return null;
					}
		} else {
			return null;
		}
	}

	public String editClaims(String token, String email) throws Exception {
		try {
			final Claims claims = getClaimsFromToken(token);
			claims.put("sub", email);
			return generateToken(claims);
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}
	}

	public String getUsernameFromToken(String token) throws UnAuthorisedUserException {
		String username = null;
		if (token != null) {
			try {
				final Claims claims = getClaimsFromToken(token);
				if (claims != null) {
					username = claims.getSubject();
					if (canTokenBeRefreshed(token, getCreatedDateFromToken(token))) {
						refreshToken(token);
					}
				}
			
			} catch (final Exception e) {
				log.error(e.getMessage());
				throw new UnAuthorisedUserException(invalidToken);
			}
		}
		return username;
	}

	public Date getCreatedDateFromToken(String token) throws UnAuthorisedUserException {
		Date created;
		try {
			final Claims claims = getClaimsFromToken(token);
			
			created = new Date((Long) claims.get(AuthTokenUtil.created));
			
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}
		return created;
	}

	public Date getExpirationDateFromToken(String token) throws UnAuthorisedUserException {
		Date expiration;
		try {
			final Claims claims = getClaimsFromToken(token);
			expiration = claims.getExpiration();
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}
		return expiration;
	}

	public String getAudienceFromToken(String token) throws UnAuthorisedUserException {
		String audience;
		try {
			final Claims claims = getClaimsFromToken(token);
			audience = (String) claims.get("audience");
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}
		return audience;
	}

	private Claims getClaimsFromToken(String token) throws UnAuthorisedUserException {
		Claims claims;
		try {
			generateSecret();
			claims = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}
		return claims;
	}

	private void generateSecret() {
		if (secret == null || secret.isEmpty()) {
			final SecureRandom random = new SecureRandom();
			final byte[] keyBytes = new byte[256];
			random.nextBytes(keyBytes);
			secret = Arrays.toString(keyBytes);
		}

	}

	private Date generateCurrentDate() {
		return new Date();
	}

	private Date generateExpirationDate() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, 30);
		return new Date(cal.getTimeInMillis());
	}

	private Boolean isTokenExpired(String token) throws UnAuthorisedUserException {
		return getExpirationDateFromToken(token).before(generateCurrentDate());
	}

	private Boolean isCreatedBeforeLastPasswordReset(Date created, Date lastPasswordReset) {
		return lastPasswordReset != null && (created.before(lastPasswordReset) || created.equals(lastPasswordReset));
	}

	private String generateAudience(Device device) {
		String audience = AUDIENCE_UNKNOWN;
		if (device == null) {
			return audience;
		} else if (device.isNormal()) {

			audience = AUDIENCE_WEB;
		} else if (device.isTablet()) {
			audience = AUDIENCE_TABLET;
		} else if (device.isMobile()) {
			audience = AUDIENCE_MOBILE;
		}
		return audience;
	}

	private Boolean ignoreTokenExpiration(String token) throws UnAuthorisedUserException {
		final String audience = getAudienceFromToken(token);
		return AUDIENCE_TABLET.equals(audience) || AUDIENCE_MOBILE.equals(audience);
	}

	public String generateToken(UserDetails userDetails, Device device) throws Exception {
		final Map<String, Object> claims = new HashMap<>();
		claims.put("sub", userDetails.getUsername());
		claims.put("audience", generateAudience(device));
		claims.put(created, generateCurrentDate());
		claims.put("reset", generateCurrentDate());
		return this.generateToken(claims);
	}

	private String generateToken(Map<String, Object> claims) {
		generateSecret();
		return Jwts.builder().setClaims(claims).setExpiration(generateExpirationDate())
				.signWith(SignatureAlgorithm.HS512, secret).compact();
	}

	public Date getResetDateFromToken(String token) throws Exception {
		Date reset = null;
		try {
			final Claims claims = getClaimsFromToken(token);

			reset = new Date((Long) claims.get("reset"));

		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new ExpectationFailedException(e.getMessage());
		}
		return reset;
	}

	public Boolean canTokenBeRefreshed(String token) throws Exception {
		final Date created = getCreatedDateFromToken(token);
		final Date lastPasswordReset = getResetDateFromToken(token);
		return isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean canTokenBeRefreshed(String token, Date lastPasswordReset) throws UnAuthorisedUserException {
		final Date created = getCreatedDateFromToken(token);
		return isCreatedBeforeLastPasswordReset(created, lastPasswordReset)
				&& (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public String refreshToken(String token) throws UnAuthorisedUserException {
		String refreshedToken;
		try {
			final Claims claims = getClaimsFromToken(token);
			refreshedToken = this.generateToken(claims);
		} catch (final Exception e) {
			log.error(e.getMessage());
			throw new UnAuthorisedUserException(invalidToken);
		}

		return refreshedToken;
	}

	public Boolean validateToken(String token, UserDetails userDetails) throws UnAuthorisedUserException {
		final User user = (User) userDetails;
		final String username = userDetails.getUsername();
		if (username.equals(user.getUsername()) && !isTokenExpired(token)) {
			return true;
		}
		return false;
	}
	@Transactional(rollbackFor = { Exception.class })
	public void loadUser(String username, Optional<String> authToken, HttpServletRequest httpRequest)
			throws UnAuthorisedUserException {
		final UserDetails userDetails = userService.loadUserByUserName(username);
		if (validateToken(authToken.orElse(null), userDetails)) {

			final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
					userDetails, null, userDetails.getAuthorities());
			authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpRequest));
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
}
}
