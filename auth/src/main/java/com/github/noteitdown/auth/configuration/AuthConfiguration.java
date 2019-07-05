package com.github.noteitdown.auth.configuration;

import com.github.noteitdown.auth.repository.UserRepository;
import com.github.noteitdown.auth.security.JwtTokenProvider;
import com.github.noteitdown.auth.service.UserService;
import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Created by cenkakin
 */
@Configuration
public class AuthConfiguration {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public JwtTokenProvider jwtTokenProvider(JwtProperties jwtProperties) {
        return new JwtTokenProvider(jwtProperties);
    }

    @Bean
    public UserService userService(UserRepository userRepository, PasswordEncoder encoder) {
        return new UserService(userRepository, encoder);
    }
}
