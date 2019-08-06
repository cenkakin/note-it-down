package com.github.noteitdown.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

class AuthorizationHeaderPayloadExtractor implements Function<ServerWebExchange, Mono<String>> {

	@Override
	public Mono<String> apply(ServerWebExchange exchange) {
		return Mono.justOrEmpty(exchange.getRequest()
			.getHeaders()
			.getFirst(HttpHeaders.AUTHORIZATION));
	}
}
