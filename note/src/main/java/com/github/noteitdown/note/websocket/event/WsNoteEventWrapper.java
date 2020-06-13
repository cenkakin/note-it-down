package com.github.noteitdown.note.websocket.event;

import com.github.noteitdown.note.model.User;

public class WsNoteEventWrapper {

    private final User user;

    private final WsNoteEvent wsNoteEvent;

    public WsNoteEventWrapper(User user, WsNoteEvent wsNoteEvent) {
        this.user = user;
        this.wsNoteEvent = wsNoteEvent;
    }

    public User getUser() {
        return user;
    }

    public WsNoteEvent getWsNoteEvent() {
        return wsNoteEvent;
    }

    @Override
    public String toString() {
        return "WsNoteEventWrapper{" +
            "user=" + user +
            ", wsNoteEvent=" + wsNoteEvent +
            '}';
    }
}
