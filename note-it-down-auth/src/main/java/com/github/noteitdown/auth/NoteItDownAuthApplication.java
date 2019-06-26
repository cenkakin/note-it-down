package com.github.noteitdown.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableEurekaClient
public class NoteItDownAuthApplication {

	public static void main(String[] args) {
		SpringApplication.run(NoteItDownAuthApplication.class, args);
	}
}
