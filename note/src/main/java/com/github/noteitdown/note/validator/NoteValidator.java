package com.github.noteitdown.note.validator;

import com.github.noteitdown.note.validator.event.NoteValidatedInternalEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.ConnectableFlux;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

public class NoteValidator {

    private static final Logger LOG = LoggerFactory.getLogger(NoteValidator.class);

    private final Flux<WsNoteEventWrapper> noteEventPublisher;

    private final ApplicationEventPublisher eventPublisher;

    public NoteValidator(Flux<WsNoteEventWrapper> noteEventPublisher, ApplicationEventPublisher eventPublisher) {
        this.noteEventPublisher = noteEventPublisher
                .replay(25)
                .autoConnect();
        this.eventPublisher = eventPublisher;
    }

    @PostConstruct
    public void init() {
        process();
    }

    private void process() {
        noteEventPublisher
                .log()
                .map(NoteValidatedInternalEvent::of)
                .doOnNext(eventPublisher::publishEvent)
                .subscribe();
    }
}
