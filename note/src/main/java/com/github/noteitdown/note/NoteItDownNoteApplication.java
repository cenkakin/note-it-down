package com.github.noteitdown.note;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class NoteItDownNoteApplication {

    public static void main(String[] args) {
        SpringApplication.run(NoteItDownNoteApplication.class, args);
    }
}
