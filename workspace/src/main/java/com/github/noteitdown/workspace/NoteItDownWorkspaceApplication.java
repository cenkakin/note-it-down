package com.github.noteitdown.workspace;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NoteItDownWorkspaceApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItDownWorkspaceApplication.class, args);
    }
}
