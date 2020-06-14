package com.github.noteitdown.note.application.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;

/**
 * Created by cenkakin
 */
@Getter
public class NoteStatusDto {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final Long transactionId;

    private final NoteStatus noteStatus;

    private final String errorMessage;

    private NoteStatusDto(Long transactionId, NoteStatus noteStatus) {
        this(transactionId, noteStatus, null);
    }

    private NoteStatusDto(Long transactionId, NoteStatus noteStatus, String errorMessage) {
        this.transactionId = transactionId;
        this.noteStatus = noteStatus;
        this.errorMessage = errorMessage;
    }

    public enum NoteStatus {
        PROCESSED, ERROR
    }

    public static NoteStatusDto successfulEvent(Long transactionId) {
        return new NoteStatusDto(transactionId, NoteStatus.PROCESSED);
    }

    public static NoteStatusDto unsuccessfulEvent(Long transactionId, String errorMessage) {
        return new NoteStatusDto(transactionId, NoteStatus.ERROR, errorMessage);
    }

    public String toStringJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error Happened! " + e);
        }
    }
}
