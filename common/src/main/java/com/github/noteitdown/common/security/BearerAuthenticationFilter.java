package com.github.noteitdown.common.security;

import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

public class BearerAuthenticationFilter extends AuthenticationWebFilter {

	public BearerAuthenticationFilter(JwtProperties jwtProperties) {
		super(Mono::just);
		final ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter(jwtProperties);
		setServerAuthenticationConverter(bearerConverter);
	}
}
