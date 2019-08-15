package com.github.noteitdown.note.message;

import com.github.noteitdown.note.message.event.NoteMessageSentInternalEvent;
import com.github.noteitdown.note.validator.event.NoteValidatedInternalEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.DirectProcessor;

@EnableBinding(Source.class)
public class NoteEventSender {

    private final DirectProcessor<WsNoteEventWrapper> userNoteEventValidatedPublisher = DirectProcessor.create();

    private final ApplicationEventPublisher eventPublisher;

    public NoteEventSender(ApplicationEventPublisher eventPublisher) {
        this.eventPublisher = eventPublisher;
    }

    @StreamEmitter
    public void emitter(@Output(Source.OUTPUT) FluxSender fluxSender) {
        fluxSender.send(userNoteEventValidatedPublisher
                .doOnNext(e -> eventPublisher.publishEvent(NoteMessageSentInternalEvent.of(e)))
        ).subscribe();
    }

    @EventListener
    public void onEvent(NoteValidatedInternalEvent event) {
        userNoteEventValidatedPublisher.onNext(event.getWsNoteEventWrapper());
    }
}
