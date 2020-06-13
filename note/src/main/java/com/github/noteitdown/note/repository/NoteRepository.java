package com.github.noteitdown.note.repository;

import com.github.noteitdown.note.domain.Note;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface NoteRepository extends ReactiveMongoRepository<Note, String> {
}
