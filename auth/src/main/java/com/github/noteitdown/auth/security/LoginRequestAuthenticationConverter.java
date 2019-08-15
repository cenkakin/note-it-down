package com.github.noteitdown.auth.security;

import com.github.noteitdown.auth.request.LoginRequest;
import org.springframework.core.ResolvableType;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Collections;

public class LoginRequestAuthenticationConverter implements ServerAuthenticationConverter {

    private final ResolvableType usernamePasswordType = ResolvableType.forClass(LoginRequest.class);

    private final ServerCodecConfigurer serverCodecConfigurer;

    public LoginRequestAuthenticationConverter(ServerCodecConfigurer serverCodecConfigurer) {
        this.serverCodecConfigurer = serverCodecConfigurer;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        final ServerHttpRequest request = exchange.getRequest();

        MediaType contentType = request.getHeaders().getContentType();

        if (MediaType.APPLICATION_JSON.includes(contentType)) {
            return serverCodecConfigurer.getReaders().stream()
                    .filter(reader -> reader.canRead(this.usernamePasswordType, MediaType.APPLICATION_JSON))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("No JSON reader for LoginRequest"))
                    .readMono(this.usernamePasswordType, request, Collections.emptyMap())
                    .cast(LoginRequest.class)
                    .map(l -> new UsernamePasswordAuthenticationToken(l.getUsername(), l.getPassword()));
        } else {
            return Mono.empty();
        }
    }
}
