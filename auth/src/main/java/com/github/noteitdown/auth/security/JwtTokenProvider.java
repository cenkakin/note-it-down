package com.github.noteitdown.auth.security;

import com.github.noteitdown.common.security.Identity;
import com.github.noteitdown.common.security.JwtProperties;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import java.util.Date;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

public class JwtTokenProvider {

    private final JwtProperties jwtProperties;

    public JwtTokenProvider(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    public String generateToken(Authentication authentication) {
        long now = System.currentTimeMillis();
        JwtBuilder jwtBuilder = Jwts.builder()
            .setSubject(authentication.getName())
            .claim("authorities", authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
            .setIssuedAt(new Date(now))
            .setExpiration(new Date(now + jwtProperties.getExpiration() * 1000))  // in milliseconds
            .signWith(SignatureAlgorithm.HS512, jwtProperties.getSecret().getBytes());
        Object principal = authentication.getPrincipal();
        if (Identity.class.isAssignableFrom(principal.getClass())) {
            jwtBuilder.claim("id", ((Identity) principal).getId());
        }
        return jwtBuilder
            .compact();
    }
}
