package com.github.noteitdown.workspace.domain;

public class Note {

    private final String id;

    private final String content;

    private final String parentId;

    private final Boolean done;

    private final Long transactionId;

    public Note(String id, String content, String parentId, Boolean done, Long transactionId) {
        this.id = id;
        this.content = content;
        this.parentId = parentId;
        this.done = done;
        this.transactionId = transactionId;
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

    public Boolean getDone() {
        return done;
    }

    public Long getTransactionId() {
        return transactionId;
    }
}
