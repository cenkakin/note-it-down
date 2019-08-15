package com.github.noteitdown.note.websocket.event;

import com.github.noteitdown.note.model.User;

public class UserNoteEvent {

    private final User user;

    private final NoteEvent noteEvent;

    public UserNoteEvent(User user, NoteEvent noteEvent) {
        this.user = user;
        this.noteEvent = noteEvent;
    }

    public User getUser() {
        return user;
    }

    public NoteEvent getNoteEvent() {
        return noteEvent;
    }

    @Override
    public String toString() {
        return "UserNoteEvent{" +
                "user=" + user +
                ", noteEvent=" + noteEvent +
                '}';
    }
}
