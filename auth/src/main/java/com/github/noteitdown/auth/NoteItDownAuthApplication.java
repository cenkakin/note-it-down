package com.github.noteitdown.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import reactor.core.publisher.Hooks;

@SpringBootApplication
@EnableEurekaClient
@EnableMongoAuditing
public class NoteItDownAuthApplication {

    public static void main(String[] args) {
        Hooks.onOperatorDebug();
        SpringApplication.run(NoteItDownAuthApplication.class, args);
    }
}
