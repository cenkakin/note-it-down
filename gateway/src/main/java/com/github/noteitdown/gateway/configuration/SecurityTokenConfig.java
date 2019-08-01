package com.github.noteitdown.gateway.configuration;

import com.github.noteitdown.gateway.security.jwt.JwtProperties;
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
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig {

	private final JwtProperties jwtProperties;

	public SecurityTokenConfig(JwtProperties jwtProperties) {
		this.jwtProperties = jwtProperties;
	}

	@Bean
	public UrlBasedCorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addExposedHeader(HttpHeaders.SET_COOKIE);
		corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfiguration);
		return source;
	}

	@Bean
	public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
		return http.cors()
			.and()
			.csrf().disable()
			.exceptionHandling()
			.and()
			//.addFilterAt(new JwtTokenAuthenticationFilter(jwtProperties), UsernamePasswordAuthenticationFilter.class)
			.authorizeExchange()
			.pathMatchers("/actuator/**").permitAll()
			.pathMatchers(HttpMethod.POST, "/auth/login").permitAll()
			.pathMatchers(HttpMethod.POST, "/auth/users").permitAll()
			.anyExchange().authenticated()
			.and()
			.addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
			.build();
	}

	private AuthenticationWebFilter bearerAuthenticationFilter() {
		AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(Mono::just);
		ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter(jwtProperties);
		bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
		bearerAuthenticationFilter.setRequiresAuthenticationMatcher(ServerWebExchangeMatchers.pathMatchers("/api/**"));
		return bearerAuthenticationFilter;
	}
}
