package com.github.noteitdown.note.processor.event;

import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;

public class NoteMessageSentInternalEvent {

    private final WsNoteEventWrapper wsNoteEventWrapper;

    private NoteMessageSentInternalEvent(WsNoteEventWrapper wsNoteEventWrapper) {
        this.wsNoteEventWrapper = wsNoteEventWrapper;
    }

    public WsNoteEventWrapper getWsNoteEventWrapper() {
        return wsNoteEventWrapper;
    }

    public static NoteMessageSentInternalEvent of(WsNoteEventWrapper wsNoteEventWrapper) {
        return new NoteMessageSentInternalEvent(wsNoteEventWrapper);
    }
}