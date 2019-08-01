/*
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included
 * in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT. IN NO EVENT SHALL
 * THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.github.noteitdown.gateway.configuration;

import com.github.noteitdown.gateway.security.jwt.AuthorizationHeaderPayload;
import com.github.noteitdown.gateway.security.jwt.JwtProperties;
import com.github.noteitdown.gateway.security.jwt.SubjectBearer;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.function.Function;
import java.util.function.Predicate;

public class ServerHttpBearerAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProperties jwtProperties;

    private final Predicate<String> matchBearerLength;
    private final Function<String, Mono<String>> isolateBearerValue;

    public ServerHttpBearerAuthenticationConverter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        String prefix = jwtProperties.getPrefix();
        matchBearerLength = authValue -> authValue.length() > prefix.length();
        isolateBearerValue = authValue -> Mono.justOrEmpty(authValue.substring(prefix.length()));
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange serverWebExchange) {
        return Mono.justOrEmpty(serverWebExchange)
                .flatMap(AuthorizationHeaderPayload::extract)
                .filter(matchBearerLength)
                .flatMap(isolateBearerValue)
                .flatMap(token -> Mono.justOrEmpty(getClaims(token)))
                .map(SubjectBearer::create)
                .log();
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
}
