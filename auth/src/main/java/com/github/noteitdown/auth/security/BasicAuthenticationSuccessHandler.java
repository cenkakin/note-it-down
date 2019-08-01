package com.github.noteitdown.auth.security;

import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

public class BasicAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties jwtProperties;

    BasicAuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerWebExchange exchange = webFilterExchange.getExchange();
        exchange.getResponse()
                .getHeaders()
                .add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication));
        return webFilterExchange.getChain().filter(exchange);
    }

    private String getHttpAuthHeaderValue(Authentication authentication) {
        return String.join(" ",
                jwtProperties.getPrefix(),
                jwtTokenProvider.generateToken(authentication));
    }
}
