package com.github.noteitdown.auth;

import com.github.noteitdown.auth.domain.User;
import com.github.noteitdown.auth.repository.UserRepository;
import com.github.noteitdown.common.security.UserRole;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationStartedEvent;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.event.EventListener;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Set;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
public class NoteItDownAuthApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItDownAuthApplication.class, args);
    }

    @Autowired
    UserRepository userRepository;

    @EventListener(ApplicationStartedEvent.class)
    public void run() {
        userRepository.deleteAll();
        User user = new User();
        user.setEmail("cenk@akin.com");
        user.setPassword(new BCryptPasswordEncoder().encode("password"));
        user.setActive(true);
        user.setRoles(Set.of(UserRole.USER));
        userRepository.save(user);
    }
}
