package com.saf.hackathon.config.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Optional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.filter.OncePerRequestFilter;

import com.saf.hackathon.exception.UnAuthorisedUserException;
import com.saf.hackathon.pojo.APIError;
import com.saf.hackathon.service.UserAuthService;
import com.saf.hackathon.service.UtilService;
import com.saf.hackathon.util.AuthTokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;

//@Component
//@Order(Ordered.HIGHEST_PRECEDENCE)
public class AuthTokenFilter extends OncePerRequestFilter {

	@Value("${token.header}")
	private String tokenHeader;

	@Value("${source.header.value}")
	private String src;
	
	@Autowired
	private AuthTokenUtil tokenUtils;

	@Autowired
	private UserAuthService userService;

	@Autowired
	private UtilService utilService;
	@Autowired
	private Environment env;
	private final Logger log = LoggerFactory.getLogger(AuthTokenFilter.class);

	/*
	 * (non-Javadoc)
	 *
	 * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest,
	 * javax.servlet.ServletResponse, javax.servlet.FilterChain)
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
			throws ServletException, IOException {

		try {
			final HttpServletRequest httpRequest = request;
			if (!isAsyncStarted(request) && !isAsyncDispatch(request)
					&& !httpRequest.getMethod().equals(String.valueOf(HttpMethod.OPTIONS))) {
				final Optional<String> authToken = Optional.ofNullable(httpRequest.getHeader("origin-api"));

				 System.out.println("token: " + authToken.orElse(null));
				final String username = tokenUtils.getUsernameFromToken(authToken.orElse(null));
				if (username == null) {
					throw new UnAuthorisedUserException(env.getProperty("invalid.token"));
				}

				final HttpServletResponse httpResponse = response;
				httpResponse.setHeader("ORIGIN-API", authToken.orElse(null));
				tokenUtils.loadUser(username, authToken, httpRequest);
			}
			chain.doFilter(request, response);
		} catch (final UnAuthorisedUserException ex) {
			log.error(ex.getMessage());

			final ObjectMapper map = new ObjectMapper();
			final APIError apiError = new APIError();
			apiError.setMsgDeveloper(ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage()
					: utilService.getStackTrace(ex));
			apiError.setMsgUser(ex.getMessage());
			apiError.setCode(String.valueOf(HttpStatus.UNAUTHORIZED));
			final HttpServletResponse response2 = utilService.addCORSHeaders(response);
			response2.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
			response2.setStatus(Integer.valueOf(401));
			response2.getWriter().write(map.writeValueAsString(apiError));

		} catch (final Exception ex) {
			log.error(ex.getMessage());
			final ObjectMapper map = new ObjectMapper();
			final APIError apiError = new APIError();
			apiError.setMsgDeveloper(ex.getMessage() != null && !ex.getMessage().isEmpty() ? ex.getMessage()
					: utilService.getStackTrace(ex));
			apiError.setMsgUser(env.getProperty("saf.error.message"));
			apiError.setCode(String.valueOf(HttpStatus.INTERNAL_SERVER_ERROR));
			final HttpServletResponse response2 = response;
			response2.setContentType(String.valueOf(MediaType.APPLICATION_JSON));
			response2.setStatus(Integer.valueOf(500));
			response2.getWriter().write(map.writeValueAsString(apiError));

		}
	}

	@Override
	public void destroy() {
		// do nothing

	}

}
