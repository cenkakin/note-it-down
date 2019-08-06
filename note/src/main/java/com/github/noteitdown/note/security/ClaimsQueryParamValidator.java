package com.github.noteitdown.note.security;

import io.jsonwebtoken.Claims;
import org.springframework.web.server.ServerWebExchange;

import java.util.function.BiFunction;

public class ClaimsQueryParamValidator implements BiFunction<Claims, ServerWebExchange, Boolean> {

	@Override
	public Boolean apply(Claims claims, ServerWebExchange exchange) {
		return claims.getSubject().equals(exchange.getRequest().getQueryParams().getFirst("subject"));
	}
}
