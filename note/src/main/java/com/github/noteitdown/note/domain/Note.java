package com.github.noteitdown.note.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@Getter
@Setter
public class Note {

    //Use userId as an id for now, will change the design in the future
    @Id
    private String userId;

    private Object content;

    private Long transactionId;
}
