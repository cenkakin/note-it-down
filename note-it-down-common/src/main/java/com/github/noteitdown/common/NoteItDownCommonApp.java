package com.github.noteitdown.common;

import com.github.noteitdown.common.security.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NoteItDownCommonApp {

	public static void main(String[] args) {
		SpringApplication.run(NoteItDownCommonApp.class, args);
	}
}
