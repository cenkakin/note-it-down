package com.github.noteitdown.note.message.event;

import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;

/**
 * Created by cenkakin
 */
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
