package com.github.noteitdown.note.security;

import com.github.noteitdown.common.security.BearerAuthenticationFilter;
import com.github.noteitdown.common.security.JwtProperties;
import com.github.noteitdown.common.security.QueryParamAuthenticationFilter;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http, JwtProperties jwtProperties) {
        return http.cors()
                .and()
                .csrf().disable()
                .exceptionHandling()
                .and()
                .authorizeExchange()
                .pathMatchers("/actuator/**").permitAll()
                .anyExchange().authenticated()
                .and()
                .addFilterAt(new QueryParamAuthenticationFilter(jwtProperties), SecurityWebFiltersOrder.AUTHENTICATION)
                .addFilterAt(new BearerAuthenticationFilter(jwtProperties), SecurityWebFiltersOrder.AUTHENTICATION)
                .build();
    }
}
