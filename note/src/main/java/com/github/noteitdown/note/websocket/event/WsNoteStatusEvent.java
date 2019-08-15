package com.github.noteitdown.note.websocket.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Created by cenkakin
 */
public class WsNoteStatusEvent {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final WsNoteEvent wsNoteEvent;

    private final NoteStatus noteStatus;

    private final String errorMessage;

    private WsNoteStatusEvent(WsNoteEvent wsNoteEvent, NoteStatus noteStatus) {
        this(wsNoteEvent, noteStatus, null);
    }

    private WsNoteStatusEvent(WsNoteEvent wsNoteEvent, NoteStatus noteStatus, String errorMessage) {
        this.wsNoteEvent = wsNoteEvent;
        this.noteStatus = noteStatus;
        this.errorMessage = errorMessage;
    }

    public WsNoteEvent getWsNoteEvent() {
        return wsNoteEvent;
    }

    public NoteStatus getNoteStatus() {
        return noteStatus;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public enum NoteStatus {
        PROCESSED, ERROR
    }

    public static WsNoteStatusEvent successfulEvent(WsNoteEvent event) {
        return new WsNoteStatusEvent(event, NoteStatus.PROCESSED);
    }

    public static WsNoteStatusEvent unsuccessfulEvent(WsNoteEvent event, String errorMessage) {
        return new WsNoteStatusEvent(event, NoteStatus.ERROR, errorMessage);
    }

    public String toStringJson() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Error Happened! " + e);
        }
    }
}
