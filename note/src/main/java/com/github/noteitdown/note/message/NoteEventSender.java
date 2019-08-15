package com.github.noteitdown.note.message;

import com.github.noteitdown.note.processor.event.NoteValidatedInternalEvent;
import com.github.noteitdown.note.websocket.event.UserNoteEvent;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.annotation.Output;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.cloud.stream.reactive.FluxSender;
import org.springframework.cloud.stream.reactive.StreamEmitter;
import org.springframework.context.event.EventListener;
import reactor.core.publisher.DirectProcessor;

@EnableBinding(Source.class)
public class NoteEventSender {

    private DirectProcessor<UserNoteEvent> userNoteEventValidatedPublisher = DirectProcessor.create();

    @StreamEmitter
    public void setCreatedFluxSender(@Output(Source.OUTPUT) FluxSender fluxSender) {
        fluxSender.send(userNoteEventValidatedPublisher.log()).subscribe();
    }

    @EventListener
    public void onEvent(NoteValidatedInternalEvent event) {
        userNoteEventValidatedPublisher.onNext(event.getUserNoteEvent());
    }
}
