package com.github.noteitdown.note.security;

import com.github.noteitdown.common.security.JwtProperties;
import com.github.noteitdown.common.security.ServerHttpBearerAuthenticationConverter;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import reactor.core.publisher.Mono;

class QueryParamAuthenticationFilter extends AuthenticationWebFilter {

	QueryParamAuthenticationFilter(JwtProperties jwtProperties) {
		super(Mono::just);
		final ServerAuthenticationConverter bearerConverter =
			new ServerHttpBearerAuthenticationConverter(jwtProperties,
				new AuthorizationQueryParamExtractor(),
				new ClaimsQueryParamValidator());
		setServerAuthenticationConverter(bearerConverter);
	}
}
