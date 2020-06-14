package com.github.noteitdown.note.domain.note.service;

import com.github.noteitdown.note.domain.note.IncomingNote;
import com.github.noteitdown.note.domain.note.Note;
import com.github.noteitdown.note.domain.note.exception.NoteNotFoundException;
import com.github.noteitdown.note.domain.note.exception.OutdatedNoteException;
import com.github.noteitdown.note.domain.note.repository.NoteRepository;
import lombok.AllArgsConstructor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
public class NoteService {

    private final NoteRepository noteRepository;

    public Mono<Note> upsertIfNewer(IncomingNote incomingNote) {
        final Note newNote = getNote(incomingNote);
        return noteRepository.findById(newNote.getUserId())
            .flatMap(noteInDb -> {
                if (noteInDb != null && noteInDb.getTransactionId() > newNote.getTransactionId()) {
                    return Mono.error(new OutdatedNoteException(newNote.getUserId(), newNote.getTransactionId(), noteInDb.getTransactionId()));
                }
                return noteRepository.save(newNote);
            })
            .switchIfEmpty(noteRepository.save(newNote));
    }

    private Note getNote(IncomingNote incomingNote) {
        Note newNote = new Note();
        newNote.setContent(incomingNote.getContent());
        newNote.setTransactionId(incomingNote.getTransactionId());
        newNote.setUserId(incomingNote.getUserId());
        return newNote;
    }

    public Mono<Note> getNote(String userId) {
        return noteRepository.findById(userId)
            .switchIfEmpty(Mono.error(new NoteNotFoundException()));
    }
}
