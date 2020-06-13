package com.github.noteitdown.auth.service;

import com.github.noteitdown.auth.domain.User;
import com.github.noteitdown.auth.exception.EmailAlreadyExistsException;
import com.github.noteitdown.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import reactor.core.publisher.Mono;

/**
 * Created by cenkakin
 */
public class UserService implements ReactiveUserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public Mono<UserDetails> findByUsername(String username) {
        return loadByEmail(username)
            .map(user -> user);
    }

    private Mono<User> loadByEmail(String email) {
        return userRepository.findByEmail(email.toLowerCase());
    }

    public Mono<Object> registerUser(User user) {
        user.setEmail(user.getEmail().toLowerCase());
        return userRepository.findByEmail(user.getEmail())
            .flatMap(u -> Mono.error(new EmailAlreadyExistsException()))
            .switchIfEmpty(Mono.defer(() -> {
                user.setPassword(encoder.encode(user.getPassword()));
                return userRepository.insert(user);
            }));
    }
}
