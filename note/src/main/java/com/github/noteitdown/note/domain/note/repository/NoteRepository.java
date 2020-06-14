package com.github.noteitdown.note.domain.note.repository;

import com.github.noteitdown.note.domain.note.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NoteRepository extends ReactiveMongoRepository<Note, String> {
}
