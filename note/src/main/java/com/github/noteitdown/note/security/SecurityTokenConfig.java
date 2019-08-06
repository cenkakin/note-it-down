package com.github.noteitdown.note.security;

import com.github.noteitdown.common.security.BearerAuthenticationFilter;
import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

@EnableWebFluxSecurity
@EnableConfigurationProperties(JwtProperties.class)
public class SecurityTokenConfig {

    @Bean
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        final CorsConfiguration corsConfiguration = new CorsConfiguration();
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.addAllowedHeader("*");
        corsConfiguration.addAllowedMethod("*");
        corsConfiguration.addAllowedOrigin("*");
        corsConfiguration.addExposedHeader(HttpHeaders.SET_COOKIE);
        corsConfiguration.addExposedHeader(HttpHeaders.AUTHORIZATION);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }

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
