package com.github.noteitdown.note.security;

import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class AuthorizationQueryParamExtractor implements Function<ServerWebExchange, Mono<String>> {

	@Override
	public Mono<String> apply(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest()
			.getQueryParams()
			.getFirst("token"));
	}
}
