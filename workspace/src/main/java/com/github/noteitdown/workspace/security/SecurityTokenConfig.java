package com.github.noteitdown.workspace.security;

import com.github.noteitdown.common.security.JwtProperties;
import com.github.noteitdown.common.security.ServerHttpBearerAuthenticationConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig {

	private final JwtProperties jwtProperties;

	public SecurityTokenConfig(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http
			.csrf().disable()
			.exceptionHandling()
			.and()
			.authorizeExchange()
			.pathMatchers("/actuator/**").permitAll()
			.anyExchange()
				.authenticated()
			.and()
			.addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
			.build();
	}

	private AuthenticationWebFilter bearerAuthenticationFilter() {
		final AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(Mono::just);
		final ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter(jwtProperties);
		bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
		return bearerAuthenticationFilter;
	}
}
