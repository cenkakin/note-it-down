package com.github.noteitdown.note.websocket.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Value;

@Value
@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@AllArgsConstructor
public class WsNoteEvent {

    private final static ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    Long transactionId;

    Object content;

    //TODO
    //private List<NoteOperation> operations;

    //TODO
    //public static class NoteOperation {
    //
    //    private String noteId;
    //
    //    private String parentNoteId;
    //
    //    private Boolean completed;
    //
    //    private String type;
    //
    //    private String content;
    //
    //    public String getNoteId() {
    //        return noteId;
    //    }
    //
    //    public void setNoteId(String noteId) {
    //        this.noteId = noteId;
    //    }
    //
    //    public String getParentNoteId() {
    //        return parentNoteId;
    //    }
    //
    //    public void setParentNoteId(String parentNoteId) {
    //        this.parentNoteId = parentNoteId;
    //    }
    //
    //    public Boolean getCompleted() {
    //        return completed;
    //    }
    //
    //    public void setCompleted(Boolean completed) {
    //        this.completed = completed;
    //    }
    //
    //    public String getType() {
    //        return type;
    //    }
    //
    //    public void setType(String type) {
    //        this.type = type;
    //    }
    //
    //    public String getContent() {
    //        return content;
    //    }
    //
    //    public void setContent(String content) {
    //        this.content = content;
    //    }
    //
    //    @Override
    //    public String toString() {
    //        return "NoteOperation{" +
    //                "noteId='" + noteId + '\'' +
    //                ", parentNoteId='" + parentNoteId + '\'' +
    //                ", completed=" + completed +
    //                ", type='" + type + '\'' +
    //                ", content='" + content + '\'' +
    //                '}';
    //    }
    //}

    public static WsNoteEvent fromStringJson(String message) {
        try {
            return OBJECT_MAPPER.readValue(message, WsNoteEvent.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + message, e);
        }
    }
}
