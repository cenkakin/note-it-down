package com.github.noteitdown.note.validator.event;

import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;

public class NoteValidatedInternalEvent {

    private final WsNoteEventWrapper wsNoteEventWrapper;

    private NoteValidatedInternalEvent(WsNoteEventWrapper wsNoteEventWrapper) {
        this.wsNoteEventWrapper = wsNoteEventWrapper;
    }

    public WsNoteEventWrapper getWsNoteEventWrapper() {
        return wsNoteEventWrapper;
    }

    public static NoteValidatedInternalEvent of(WsNoteEventWrapper wsNoteEventWrapper) {
        return new NoteValidatedInternalEvent(wsNoteEventWrapper);
    }
}
