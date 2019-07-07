package com.github.noteitdown.auth.service;

import com.github.noteitdown.auth.domain.User;
import com.github.noteitdown.auth.exception.EmailAlreadyExistsException;
import com.github.noteitdown.auth.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

/**
 * Created by cenkakin
 */
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    public UserService(UserRepository userRepository, PasswordEncoder encoder) {
        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return loadByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found!"));
    }

    private Optional<User> loadByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public void registerUser(User user) {
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser.isPresent()) {
            throw new EmailAlreadyExistsException();
        }
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.insert(user);
    }
}
