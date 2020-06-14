package com.github.noteitdown.note.domain.note;

import com.github.noteitdown.note.domain.note.event.NoteProcessedEvent;
import com.github.noteitdown.note.domain.note.exception.OutdatedNoteException;
import com.github.noteitdown.note.domain.note.service.NoteService;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class IncomingNoteProcessor {

    private final DirectProcessor<IncomingNote> wsNoteEventPublisher = DirectProcessor.create();

    private final ApplicationEventPublisher applicationEventPublisher;

    private final NoteService noteService;

    @PostConstruct
    public void init() {
        subscribeProcessor();
    }

    public void subscribeProcessor() {
        wsNoteEventPublisher
            .flatMap(noteService::upsertIfNewer)
            .log()
            .map(newNote -> NoteProcessedEvent.successful(newNote.getTransactionId(), newNote.getUserId()))
            .onErrorResume(OutdatedNoteException.class,
                e -> Mono.just(NoteProcessedEvent.failed(e.getOutdatedTransactionId(), e.getUserId(), e.getMessage())))
            .doOnNext(applicationEventPublisher::publishEvent)
            .subscribe();
    }

    @EventListener(IncomingNote.class)
    public void onApplicationEvent(IncomingNote event) {
        wsNoteEventPublisher.onNext(event);
    }
}
