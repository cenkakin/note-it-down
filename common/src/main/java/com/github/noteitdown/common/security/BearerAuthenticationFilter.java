package com.github.noteitdown.common.security;

import org.springframework.http.HttpHeaders;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;

public class BearerAuthenticationFilter extends AuthenticationWebFilter {

    private final static Function<ServerWebExchange, Mono<String>> AUTHORIZATION_HEADER_PAYLOAD_EXTRACTOR =
            exchange -> Mono.justOrEmpty(exchange.getRequest()
                    .getHeaders()
                    .getFirst(HttpHeaders.AUTHORIZATION));

    public BearerAuthenticationFilter(JwtProperties jwtProperties) {
        super(Mono::just);
        final var bearerConverter =
                new ServerHttpBearerAuthenticationConverter(jwtProperties, AUTHORIZATION_HEADER_PAYLOAD_EXTRACTOR);
        setServerAuthenticationConverter(bearerConverter);
    }
}
