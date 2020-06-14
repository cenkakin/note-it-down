package com.github.noteitdown.note.domain.note;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class IncomingNote {
    String userId;

    Object content;

    Long transactionId;
}
