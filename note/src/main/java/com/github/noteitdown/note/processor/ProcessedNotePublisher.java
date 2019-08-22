package com.github.noteitdown.note.processor;

import com.github.noteitdown.note.processor.event.NoteMessageSentInternalEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.integration.support.MessageBuilder;
import reactor.core.publisher.Flux;

public final class ProcessedNotePublisher {

    public static Flux<NoteMessageSentInternalEvent> apply(Source messageBroker,
                                                           Flux<WsNoteEventWrapper> noteEventPublisher) {
        return noteEventPublisher
                .replay(25)
                .autoConnect()
                .doOnNext(e -> System.out.println("HEHEHEHE2 " + e))
                .doOnNext(e -> messageBroker.output().send(MessageBuilder.withPayload(e).build()))
                .map(NoteMessageSentInternalEvent::of);
    }
}
