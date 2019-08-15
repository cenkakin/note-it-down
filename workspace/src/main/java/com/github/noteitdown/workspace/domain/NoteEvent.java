package com.github.noteitdown.workspace.domain;

public class NoteEvent {

    private final String id;

    private final String content;

    private final String parentId;

    private final Long transactionId;

    private final NoteEventType noteEventType;

    private final Long priority;

    public NoteEvent(String id, String content, String parentId, Long transactionId, NoteEventType noteEventType, Long priority) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.transactionId = transactionId;
        this.noteEventType = noteEventType;
        this.priority = priority;
    }

    public String getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public String getParentId() {
        return parentId;
    }

    public Long getTransactionId() {
        return transactionId;
    }

    public NoteEventType getNoteEventType() {
        return noteEventType;
    }

    public Long getPriority() {
        return priority;
    }
}
