package com.github.noteitdown.note.model;

import java.net.URI;
import lombok.Value;
import org.springframework.web.util.UriComponentsBuilder;

@Value
public class User {

    String id;
    String email;

    public static User fromUri(URI uri) {
        final String email = UriComponentsBuilder.fromUri(uri)
            .build().getQueryParams().getFirst("subject");
        final String id = UriComponentsBuilder.fromUri(uri)
            .build().getQueryParams().getFirst("id");
        return new User(id, email);
    }
}
