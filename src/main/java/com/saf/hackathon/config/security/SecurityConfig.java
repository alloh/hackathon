package com.saf.hackathon.config.security;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

import com.saf.hackathon.config.filter.AuthTokenFilter;



@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

		@Bean
		public AuthTokenFilter getAuthTokenFilter() {
			return new AuthTokenFilter();
		}


		@Override
		protected void configure(final HttpSecurity http) throws Exception {
			// http.requiresChannel().anyRequest().requiresInsecure();
			http.authorizeRequests().antMatchers("/saf/**").permitAll();
			http.csrf().disable();
			http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
			// to prevent click jacking attacks
			http.headers().frameOptions().sameOrigin();
			http.headers().httpStrictTransportSecurity().disable();
			http.headers().cacheControl();
			// to prevent XSS attacks
			http.headers().xssProtection().block(false);
			// Content security Policy
			http.headers().contentSecurityPolicy("script-src 'self' ; object-src ; report-uri");
			
		}

		/* To allow Pre-flight [OPTIONS] request from browser */
		@Override
		public void configure(WebSecurity web) throws Exception {
			web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
		}

		@Bean
		public FilterRegistrationBean authenticationTokenFilterBean() throws Exception {
			final FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
			filterRegistrationBean.setFilter(getAuthTokenFilter());
			final List<String> urls = new ArrayList<>();
			urls.add("/saf/movies/*");
			filterRegistrationBean.setUrlPatterns(urls);
			// filterRegistrationBean.setEnabled(false);
			filterRegistrationBean.setOrder(Integer.MIN_VALUE);
			return filterRegistrationBean;
		}
}

