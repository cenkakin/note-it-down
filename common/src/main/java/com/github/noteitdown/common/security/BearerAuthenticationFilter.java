package com.github.noteitdown.common.security;

import java.util.function.Function;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class BearerAuthenticationFilter extends AuthenticationWebFilter {

    private final static Function<ServerWebExchange, Mono<String>> AUTHORIZATION_HEADER_PAYLOAD_EXTRACTOR =
        exchange -> Mono.justOrEmpty(exchange.getRequest()
            .getHeaders()
            .getFirst(HttpHeaders.AUTHORIZATION));

    public BearerAuthenticationFilter(JwtProperties jwtProperties) {
        super((ReactiveAuthenticationManager) Mono::just);
        final ServerAuthenticationConverter bearerConverter =
            new ServerHttpBearerAuthenticationConverter(jwtProperties, AUTHORIZATION_HEADER_PAYLOAD_EXTRACTOR);
        setServerAuthenticationConverter(bearerConverter);
    }
}
