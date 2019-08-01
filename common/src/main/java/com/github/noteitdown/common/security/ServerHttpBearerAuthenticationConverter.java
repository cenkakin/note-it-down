package com.github.noteitdown.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Created by cenkakin
 */
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProperties jwtProperties;

    private final Predicate<String> matchBearerLength;
    private final Function<String, Mono<String>> isolateBearerValue;

    public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        final String prefix = jwtProperties.getPrefix();
        matchBearerLength = authValue -> authValue.length() > prefix.length();
        isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(prefix.length()));
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(AuthorizationHeaderPayload::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(token -> Mono.justOrEmpty(getClaims(token)))
                .map(SubjectBearer::create)
                .log();
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