package com.saf.hackathon.config.security;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;

import com.saf.hackathon.model.ApplicationUser;


/**
 * Class that represents the User of the system.
 *
 * @author alagat
 *
 */
public class User extends GenericUser {
	private static final long serialVersionUID = 1L;
	private final ApplicationUser selfServiceUser;

	public User(ApplicationUser selfServiceUser, String username, String password,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, authorities);
		this.selfServiceUser = selfServiceUser;
	}

	public User(ApplicationUser selfServiceUser, String username, String password, boolean enabled,
			boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
		this.selfServiceUser = selfServiceUser;
	}

	public ApplicationUser getSelfServiceUser() {
		return selfServiceUser;
	}

}
