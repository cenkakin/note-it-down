package com.github.noteitdown.note.domain.note.event;

import lombok.Value;

/**
 * Created by cenkakin
 */
@Value
public class NoteProcessedEvent {

    Long transactionId;

    String userId;

    boolean successful;

    String errorMessage;

    public static NoteProcessedEvent successful(Long transactionId, String userId) {
        return new NoteProcessedEvent(transactionId, userId, true, null);
    }

    public static NoteProcessedEvent failed(Long transactionId, String userId, String errorMessage) {
        return new NoteProcessedEvent(transactionId, userId, false, errorMessage);
    }
}
