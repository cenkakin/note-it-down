package com.github.noteitdown.auth.security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.core.io.buffer.DataBufferFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class BasicAuthenticationSuccessHandler implements ServerAuthenticationSuccessHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;

    private final JwtProperties jwtProperties;

    BasicAuthenticationSuccessHandler(JwtTokenProvider jwtTokenProvider, JwtProperties jwtProperties) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();
        response.getHeaders().add(HttpHeaders.AUTHORIZATION, getHttpAuthHeaderValue(authentication));
        response.setStatusCode(HttpStatus.OK);
        return response.writeWith(authenticationBody(authentication.getPrincipal(), response.bufferFactory()));
    }

    private String getHttpAuthHeaderValue(Authentication authentication) {
        return String.join(" ",
            jwtProperties.getPrefix(),
            jwtTokenProvider.generateToken(authentication));
    }

    private Mono<DataBuffer> authenticationBody(Object principal, DataBufferFactory bufferFactory) {
        return Mono.just(principal)
            .map(this::getPrincipalAsBytes)
            .map(bufferFactory::wrap);
    }

    private byte[] getPrincipalAsBytes(Object principal) {
        try {
            return OBJECT_MAPPER.writeValueAsBytes(principal);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Principal couldn't be converted to bytes!");
        }
    }
}
