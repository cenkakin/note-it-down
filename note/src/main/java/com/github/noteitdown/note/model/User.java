package com.github.noteitdown.note.model;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

public class User {

    private final String id;

    private final String email;

    public User(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", email='" + email + '\'' +
                '}';
    }

    public static User fromUri(URI uri) {
        final String email = UriComponentsBuilder.fromUri(uri)
                .build().getQueryParams().getFirst("subject");
        final String id = UriComponentsBuilder.fromUri(uri)
                .build().getQueryParams().getFirst("id");
        return new User(id, email);
    }
}
