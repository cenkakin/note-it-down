package com.github.noteitdown.note.processor.event;

import com.github.noteitdown.note.websocket.event.UserNoteEvent;

public class NoteValidatedInternalEvent {

    private final UserNoteEvent userNoteEvent;

    public NoteValidatedInternalEvent(UserNoteEvent userNoteEvent) {
        this.userNoteEvent = userNoteEvent;
    }

    public UserNoteEvent getUserNoteEvent() {
        return userNoteEvent;
    }
}
