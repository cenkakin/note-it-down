package com.github.noteitdown.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

import java.net.URI;

@SpringBootApplication
@EnableDiscoveryClient
public class NoteItDownGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItDownGatewayApplication.class, args);
    }
}