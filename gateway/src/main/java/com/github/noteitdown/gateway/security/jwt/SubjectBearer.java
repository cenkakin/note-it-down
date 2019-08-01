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
package com.github.noteitdown.gateway.security.jwt;

import com.github.noteitdown.gateway.configuration.Identity;
import io.jsonwebtoken.Claims;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.stream.Collectors;

public class SubjectBearer {

    public static Authentication create(Claims claims) {
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
