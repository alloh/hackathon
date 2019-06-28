package com.saf.hackathon.config.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SafPasswordEncoder implements PasswordEncoder {
	private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	@Override
	public String encode(CharSequence arg0) {
		return passwordEncoder.encode(arg0);
	}

	@Override
	public boolean matches(CharSequence arg0, String arg1) {
		if (arg1.isEmpty()) {
			return false;
		}
		return passwordEncoder.matches(arg0, arg1);
	}
}
