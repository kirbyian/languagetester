package com.kirby.languagetester.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.accept.ContentNegotiationStrategy;
import org.springframework.web.accept.HeaderContentNegotiationStrategy;

import com.okta.spring.boot.oauth.Okta;

@Configuration
public class SecurityConfig {

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {

		// Disable CSRF
		httpSecurity.csrf().disable();

		// Protect endpoints /api/,type>/secure
		httpSecurity
				.authorizeRequests(configurer -> configurer.antMatchers("api/**").authenticated())
				.oauth2ResourceServer().jwt();

		httpSecurity.cors();

		// add content to negotiation strategy
		httpSecurity.setSharedObject(ContentNegotiationStrategy.class, new HeaderContentNegotiationStrategy());

		// force non response body for 401
		Okta.configureResourceServer401ResponseBody(httpSecurity);

		return httpSecurity.build();

	}

}
