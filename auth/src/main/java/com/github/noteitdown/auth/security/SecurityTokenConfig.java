package com.github.noteitdown.auth.security;

import com.github.noteitdown.common.security.JwtProperties;
import com.github.noteitdown.common.security.ServerHttpBearerAuthenticationConverter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UserDetailsRepositoryReactiveAuthenticationManager;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.AuthenticationWebFilter;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.security.web.server.authentication.ServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig {

    private final JwtProperties jwtProperties;

    private final ReactiveUserDetailsService userDetailsService;

    private final JwtTokenProvider jwtTokenProvider;

    private final PasswordEncoder passwordEncoder;

    public SecurityTokenConfig(JwtProperties jwtProperties,
                               ReactiveUserDetailsService userDetailsService,
                               JwtTokenProvider jwtTokenProvider,
                               PasswordEncoder passwordEncoder) {
        this.jwtProperties = jwtProperties;
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {
        return http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .addFilterAt(basicAuthenticationFilter(), SecurityWebFiltersOrder.HTTP_BASIC)
                .authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .pathMatchers(HttpMethod.POST, "/login").permitAll()
                .pathMatchers(HttpMethod.POST, "/users").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(bearerAuthenticationFilter(), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }

    private AuthenticationWebFilter bearerAuthenticationFilter() {
        final AuthenticationWebFilter bearerAuthenticationFilter = new AuthenticationWebFilter(Mono::just);
        final ServerAuthenticationConverter bearerConverter = new ServerHttpBearerAuthenticationConverter(jwtProperties);
        bearerAuthenticationFilter.setServerAuthenticationConverter(bearerConverter);
        return bearerAuthenticationFilter;
    }

    private AuthenticationWebFilter basicAuthenticationFilter() {
        ServerAuthenticationSuccessHandler successHandler =
                new BasicAuthenticationSuccessHandler(jwtTokenProvider, jwtProperties);
        UserDetailsRepositoryReactiveAuthenticationManager authManager =
                new UserDetailsRepositoryReactiveAuthenticationManager(userDetailsService);
        authManager.setPasswordEncoder(passwordEncoder);
        AuthenticationWebFilter basicAuthenticationFilter = new AuthenticationWebFilter(authManager);
        basicAuthenticationFilter.setAuthenticationSuccessHandler(successHandler);
        return basicAuthenticationFilter;
    }
}
