package com.github.noteitdown.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by cenkakin
 */
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

	private static final Function<ServerWebExchange, Mono<String>> DEFAULT_TOKEN_EXTRACTOR =
		new AuthorizationHeaderPayloadExtractor();
	private final JwtProperties jwtProperties;

	private final Predicate<String> matchBearerLength;
	private final Function<String, Mono<String>> isolateBearerValue;
	private final Function<ServerWebExchange, Mono<String>> tokenExtractor;
	private final BiFunction<Claims, ServerWebExchange, Boolean> validateClaimsInRequest;

	public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties) {
		this(jwtProperties, DEFAULT_TOKEN_EXTRACTOR,
			(claims, exchange) -> true);
	}

	public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties,
												   Function<ServerWebExchange, Mono<String>> tokenExtractor) {
		this(jwtProperties, tokenExtractor, (claims, exchange) -> true);
	}

	public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties,
												   BiFunction<Claims, ServerWebExchange, Boolean> validateClaimsInRequest) {
		this(jwtProperties, DEFAULT_TOKEN_EXTRACTOR, validateClaimsInRequest);
	}

	public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties,
												   Function<ServerWebExchange, Mono<String>> tokenExtractor,
												   BiFunction<Claims, ServerWebExchange, Boolean> validateClaimsInRequest) {
		this.jwtProperties = jwtProperties;
		this.tokenExtractor = tokenExtractor;
		this.validateClaimsInRequest = validateClaimsInRequest;
		final String prefix = jwtProperties.getPrefix();
		matchBearerLength = authValue -> authValue.length() > prefix.length();
		isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(prefix.length()));
	}

	@Override
	public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
		return Mono.justOrEmpty(serverWebExchange)
			.flatMap(tokenExtractor)
			.filter(matchBearerLength)
			.flatMap(isolateBearerValue)
			.flatMap(token -> Mono.justOrEmpty(getClaims(token)))
			.filter(claims -> validateClaimsInRequest.apply(claims, serverWebExchange))
			.map(SubjectBearer::create);
	}

	private Claims getClaims(String token) {
		Claims claims;
		try {
			claims = Jwts.parser()
				.setSigningKey(jwtProperties.getSecret().getBytes())
				.parseClaimsJws(token)
				.getBody();
		} catch (Exception e) {
			return null;
		}
		return claims.getSubject() != null ? claims : null;
	}
}
