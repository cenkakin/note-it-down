package com.github.noteitdown.workspace.domain;

import java.time.Instant;
import java.util.List;

public class WorkspaceSnapshot {

    private final String id;

    private final String userId;

    private final Instant createdAt;

    private final Instant lastModifiedAt;

    private final Long lastTransactionId;

    private final List<Note> notes;

    public WorkspaceSnapshot(String id, String userId, Instant createdAt,
                             Instant lastModifiedAt, Long lastTransactionId,
                             List<Note> notes) {
        this.id = id;
        this.userId = userId;
        this.createdAt = createdAt;
        this.lastModifiedAt = lastModifiedAt;
        this.lastTransactionId = lastTransactionId;
        this.notes = notes;
    }

    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public Instant getLastModifiedAt() {
        return lastModifiedAt;
    }

    public Long getLastTransactionId() {
        return lastTransactionId;
    }

    public List<Note> getNotes() {
        return notes;
    }
}
