package com.github.noteitdown.auth.security;

import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.http.HttpMethod;
import org.springframework.http.codec.ServerCodecConfigurer;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import org.springframework.security.web.server.util.matcher.ServerWebExchangeMatchers;

class LoginWebFilter extends AuthenticationWebFilter {

    LoginWebFilter(ReactiveAuthenticationManager manager,
                   ServerCodecConfigurer serverCodecConfigurer,
                   JwtTokenProvider jwtTokenProvider,
                   JwtProperties jwtProperties) {
        super(manager);

        setRequiresAuthenticationMatcher(
                ServerWebExchangeMatchers.pathMatchers(HttpMethod.POST, "/login")
        );
        ServerAuthenticationSuccessHandler successHandler =
                new BasicAuthenticationSuccessHandler(jwtTokenProvider, jwtProperties);

        setServerAuthenticationConverter(new LoginRequestAuthenticationConverter(serverCodecConfigurer));
        setAuthenticationSuccessHandler(successHandler);
    }
}
