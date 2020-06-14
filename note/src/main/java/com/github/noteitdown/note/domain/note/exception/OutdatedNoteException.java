package com.github.noteitdown.note.domain.note.exception;

import lombok.Getter;

@Getter
public class OutdatedNoteException extends RuntimeException {
    private final String userId;
    private final Long outdatedTransactionId;
    private final Long transactionIdInDb;

    public OutdatedNoteException(String userId, Long outdatedTransactionId, Long transactionIdInDb) {
        super("There is a newer version of workspace, please refresh!");
        this.userId = userId;
        this.outdatedTransactionId = outdatedTransactionId;
        this.transactionIdInDb = transactionIdInDb;
    }
}
