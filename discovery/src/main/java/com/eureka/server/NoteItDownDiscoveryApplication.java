package com.eureka.server;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableAdminServer
@EnableDiscoveryClient
@SpringBootApplication
@EnableEurekaServer
public class NoteItDownDiscoveryApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItDownDiscoveryApplication.class, args);
    }
}
