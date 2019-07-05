package com.github.noteitdown.auth.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noteitdown.auth.request.SigninRequest;
import com.github.noteitdown.auth.security.JwtTokenProvider;
import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;

/**
 * Authenticate the request to url /login by POST with json body '{ username, password }'.
 * If successful, response the client with header 'Authorization: Bearer jwt-token'.
 *
 * @author raj
 */
public class JwtUsernamePasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtProperties jwtProperties;

    public JwtUsernamePasswordAuthenticationFilter(JwtProperties jwtProperties,
                                                   JwtTokenProvider jwtTokenProvider,
                                                   AuthenticationManager authenticationManager) {
        super(new AntPathRequestMatcher(jwtProperties.getUri(), "POST"));
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtProperties = jwtProperties;
        super.setAuthenticationManager(authenticationManager);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse rsp) {
        SigninRequest request;
        try {
            request = OBJECT_MAPPER.readValue(req.getInputStream(), SigninRequest.class);
        } catch (IOException e) {
            throw new InsufficientAuthenticationException("Request couldn't be parsed!");
        }
        return getAuthenticationManager().authenticate(new UsernamePasswordAuthenticationToken(
                request.getUsername(), request.getPassword(), Collections.emptyList()
        ));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req, HttpServletResponse rsp, FilterChain chain,
                                            Authentication auth) {
        String token = jwtTokenProvider.generateToken(auth);
        rsp.addHeader(jwtProperties.getHeader(), jwtProperties.getPrefix() + " " + token);
    }
}
