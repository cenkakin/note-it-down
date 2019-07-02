package com.github.noteitdown.auth.configuration;

import com.github.noteitdown.auth.security.JwtTokenProvider;
import com.github.noteitdown.auth.service.UserService;
import com.github.noteitdown.common.security.JwtProperties;
import io.jsonwebtoken.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtTokenAuthenticationFilter.class);

    private final JwtProperties jwtProperties;
    private JwtTokenProvider tokenProvider;
    private UserDetailsService userService;

    public JwtTokenAuthenticationFilter(
            JwtProperties jwtProperties,
            JwtTokenProvider tokenProvider,
            UserService userService) {
        this.jwtProperties = jwtProperties;
        this.tokenProvider = tokenProvider;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String header = request.getHeader(jwtProperties.getHeader());
        if (header == null || !header.startsWith(jwtProperties.getPrefix())) {
            return;
        }
        String token = header.replace(jwtProperties.getPrefix(), "");
        if (validateToken(token)) {
            Claims claims = tokenProvider.getClaimsFromJWT(token);
            String username = claims.getSubject();
            UsernamePasswordAuthenticationToken auth = Optional.ofNullable(userService.loadUserByUsername(username))
                    .map(userDetails -> {
                        UsernamePasswordAuthenticationToken authentication =
                                new UsernamePasswordAuthenticationToken(
                                        userDetails, null, userDetails.getAuthorities());
                        authentication
                                .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                        return authentication;
                    })
                    .orElse(null);
            SecurityContextHolder.getContext().setAuthentication(auth);
        } else {
            SecurityContextHolder.clearContext();
        }
        chain.doFilter(request, response);
    }

    private boolean validateToken(String authToken) {
        try {
            Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes())
                    .parseClaimsJws(authToken);

            return true;
        } catch (SignatureException ex) {
            log.error("Invalid JWT signature");
        } catch (MalformedJwtException ex) {
            log.error("Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            log.error("Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            log.error("Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            log.error("JWT claims string is empty.");
        }
        return false;
    }
}
