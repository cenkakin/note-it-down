package com.github.noteitdown.common.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created by cenkakin
 */
public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProperties jwtProperties;
    private final Predicate<String> matchBearerLength;
    private final Function<String, Mono<String>> isolateBearerValue;
    private final Function<ServerWebExchange, Mono<String>> tokenExtractor;
    private final BiFunction<Claims, ServerWebExchange, Boolean> validateClaimsInRequest;

    public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties,
                                                   Function<ServerWebExchange, Mono<String>> tokenExtractor) {
        this(jwtProperties, tokenExtractor, (claims, exchange) -> true);
    }

    public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties,
                                                   Function<ServerWebExchange, Mono<String>> tokenExtractor,
                                                   BiFunction<Claims, ServerWebExchange, Boolean> validateClaimsInRequest) {
        this.jwtProperties = jwtProperties;
        this.tokenExtractor = tokenExtractor;
        this.validateClaimsInRequest = validateClaimsInRequest;

        final String prefix = jwtProperties.getPrefix();
        matchBearerLength = authValue -> authValue.length() > prefix.length();
        isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(prefix.length()));
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(tokenExtractor)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(token -> Mono.justOrEmpty(getClaims(token)))
                .filter(claims -> validateClaimsInRequest.apply(claims, serverWebExchange))
                .map(SubjectBearer::create);
    }

    private Claims getClaims(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(jwtProperties.getSecret().getBytes())
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            return null;
        }
        return claims.getSubject() != null ? claims : null;
    }

    static class SubjectBearer {

        static Authentication create(Claims claims) {
            List<GrantedAuthority> authorities = getAuthorities(claims);
            String id = (String) claims.get("id");
            return new UsernamePasswordAuthenticationToken(
                    new Subject(id, claims.getSubject(), authorities), null, authorities);
        }

        private static List<GrantedAuthority> getAuthorities(Claims claims) {
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
}
