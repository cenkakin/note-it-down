package com.github.noteitdown.note.domain.note;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document
@ToString
@EqualsAndHashCode
@Getter
@Setter
public class Note {

    //Use userId as an id for now, will change the design in the future
    @Id
    private String userId;

    private Object content;

    private Long transactionId;
}
