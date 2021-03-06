package com.github.noteitdown.common.security;

import io.jsonwebtoken.Claims;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class QueryParamAuthenticationFilter extends AuthenticationWebFilter {

    private final static Function<ServerWebExchange, Mono<String>> AUTHORIZATION_QUERY_PARAM_EXTRACTOR =
        exchange -> Mono.justOrEmpty(exchange.getRequest()
            .getQueryParams()
            .getFirst("token"));

    private final static BiFunction<Claims, ServerWebExchange, Boolean> CLAIMS_QUERY_PARAM_VALIDATOR =
        (claims, exchange) -> {
            String userId = (String) claims.get("id");
            return claims.getSubject().equals(exchange.getRequest().getQueryParams().getFirst("subject")) &&
                userId.equals(exchange.getRequest().getQueryParams().getFirst("id"));
        };

    public QueryParamAuthenticationFilter(JwtProperties jwtProperties) {
        super((ReactiveAuthenticationManager) Mono::just);
        final ServerAuthenticationConverter bearerConverter =
            new ServerHttpBearerAuthenticationConverter(jwtProperties,
                AUTHORIZATION_QUERY_PARAM_EXTRACTOR,
                CLAIMS_QUERY_PARAM_VALIDATOR);
        setServerAuthenticationConverter(bearerConverter);
    }
}
