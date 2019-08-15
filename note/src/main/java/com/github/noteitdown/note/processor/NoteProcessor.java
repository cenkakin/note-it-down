package com.github.noteitdown.note.processor;

import com.github.noteitdown.note.processor.event.NoteValidatedInternalEvent;
import com.github.noteitdown.note.websocket.event.UserNoteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

public class NoteProcessor {

    private static final Logger LOG = LoggerFactory.getLogger(NoteProcessor.class);

    private final Flux<UserNoteEvent> noteEventPublisher;

    private final ApplicationEventPublisher eventPublisher;

    public NoteProcessor(Flux<UserNoteEvent> noteEventPublisher, ApplicationEventPublisher eventPublisher) {
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
                .map(NoteValidatedInternalEvent::new)
                .doOnNext(eventPublisher::publishEvent)
                .subscribe();
    }
}
