package com.github.noteitdown.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

public class JwtTokenAuthenticationFilter extends OncePerRequestFilter {
    private final JwtProperties jwtProperties;

    public JwtTokenAuthenticationFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String header = request.getHeader(jwtProperties.getHeader());

        if (header == null || !header.startsWith(jwtProperties.getPrefix())) {
            chain.doFilter(request, response);
            return;
        }

        String token = header.replace(jwtProperties.getPrefix(), "");

        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
            String username = claims.getSubject();
            if (username != null) {
                @SuppressWarnings("unchecked")
                List<GrantedAuthority> authorities = getAuthorities(claims);
                String id = (String) claims.get("id");
                UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                        new Subject(id, username, authorities), null, authorities);

                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception e) {
            SecurityContextHolder.clearContext();
        }

        chain.doFilter(request, response);
    }

    private List<GrantedAuthority> getAuthorities(Claims claims) {
        return ((List<String>) claims.get("authorities"))
                .stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
    }

    public static class Subject implements Identity {

        private final String id;

        private final String username;

        private final List<GrantedAuthority> authorities;

        public Subject(String id, String username, List<GrantedAuthority> authorities) {
            this.id = id;
            this.username = username;
            this.authorities = authorities;
        }

        @Override
        public String getId() {
            return id;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }

        @Override
        public List<GrantedAuthority> getAuthorities() {
            return authorities;
        }

        @Override
        public String getPassword() {
            throw new UnsupportedOperationException("Unavailable!");
        }

        @Override
        public String toString() {
            return "Subject{" +
                    "id='" + id + '\'' +
                    ", username='" + username + '\'' +
                    ", authorities=" + authorities +
                    '}';
        }
    }
}