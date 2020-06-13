package com.github.noteitdown.note.processor;

import com.github.noteitdown.note.domain.Note;
import com.github.noteitdown.note.model.User;
import com.github.noteitdown.note.processor.event.NoteProcessedEvent;
import com.github.noteitdown.note.repository.NoteRepository;
import com.github.noteitdown.note.websocket.event.WsNoteEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import javax.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Mono;

@AllArgsConstructor
@Slf4j
public class NoteProcessor {

    private final DirectProcessor<WsNoteEventWrapper> wsNoteEventPublisher = DirectProcessor.create();

    private final ApplicationEventPublisher applicationEventPublisher;

    private final NoteRepository noteRepository;

    @PostConstruct
    public void init() {
        subscribeProcessor();
    }

    public void subscribeProcessor() {
        wsNoteEventPublisher
            .map(this::getNote)
            .flatMap(this::upsertIfNewer)
            .onErrorContinue((t, e) -> log.error("ERROR happened while processing note!", t))
            .map(NoteProcessedEvent::new)
            .doOnNext(applicationEventPublisher::publishEvent)
            .subscribe();
    }

    private Note getNote(WsNoteEventWrapper eventWrapper) {
        User user = eventWrapper.getUser();
        WsNoteEvent wsNoteEvent = eventWrapper.getWsNoteEvent();
        Note newNote = new Note();
        newNote.setContent(wsNoteEvent.getContent());
        newNote.setTransactionId(wsNoteEvent.getTransactionId());
        newNote.setUserId(user.getId());
        return newNote;
    }

    private Mono<Note> upsertIfNewer(Note newNote) {
        return noteRepository.findById(newNote.getUserId())
            .flatMap(noteInDb -> {
                if (noteInDb != null && noteInDb.getTransactionId() > newNote.getTransactionId()) {
                    return Mono.error(new IllegalArgumentException("Newer version exists"));
                }
                return noteRepository.save(newNote);
            })
            .switchIfEmpty(noteRepository.save(newNote));
    }

    @EventListener(WsNoteEventWrapper.class)
    public void onApplicationEvent(WsNoteEventWrapper event) {
        wsNoteEventPublisher.onNext(event);
    }
}
