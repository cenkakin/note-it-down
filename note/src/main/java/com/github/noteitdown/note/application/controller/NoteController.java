package com.github.noteitdown.note.application.controller;

import com.github.noteitdown.common.exception.BadRequestException;
import com.github.noteitdown.common.security.Identity;
import com.github.noteitdown.note.domain.note.Note;
import com.github.noteitdown.note.domain.note.exception.NoteNotFoundException;
import com.github.noteitdown.note.domain.note.service.NoteService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@RestController
@RequestMapping("/workspace")
public class NoteController {

    private final NoteService noteService;

    @GetMapping
    public Mono<ResponseEntity<Note>> get(@AuthenticationPrincipal Identity identity) {
        return noteService.getNote(identity.getId())
            .map(ResponseEntity::ok)
            .onErrorMap(NoteNotFoundException.class,
                e -> new BadRequestException(e.getMessage()));
    }
}
