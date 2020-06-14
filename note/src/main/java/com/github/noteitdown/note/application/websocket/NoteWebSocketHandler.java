package com.github.noteitdown.note.application.websocket;

import com.github.noteitdown.note.application.dto.IncomingNoteRequest;
import com.github.noteitdown.note.application.dto.NoteStatusDto;
import com.github.noteitdown.note.domain.note.IncomingNote;
import com.github.noteitdown.note.domain.note.NoteProcessedEventPublisher;
import com.github.noteitdown.note.domain.note.event.NoteProcessedEvent;
import com.github.noteitdown.note.domain.user.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.web.reactive.socket.WebSocketMessage.Type.TEXT;

@Slf4j
public class NoteWebSocketHandler implements WebSocketHandler {

    private final ApplicationEventPublisher eventPublisher;
    private final Flux<NoteProcessedEvent> senderFlux;

    public NoteWebSocketHandler(ApplicationEventPublisher eventPublisher, NoteProcessedEventPublisher noteProcessedEventPublisher) {
        this.eventPublisher = eventPublisher;
        senderFlux = Flux
            .create(noteProcessedEventPublisher)
            .publish()
            .autoConnect();
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        User user = User.fromUri(session.getHandshakeInfo().getUri());
        return Mono.zip(receiver(session, user), sender(session, user)).then();
    }

    private Mono<Void> sender(WebSocketSession session, User user) {
        return session.send(senderFlux
            .filter(e -> e.getUserId().equals(user.getId()))
            .log()
            .map(e -> {
                if (e.isSuccessful()) {
                    return NoteStatusDto.successfulEvent(e.getTransactionId());
                } else {
                    return NoteStatusDto.unsuccessfulEvent(e.getTransactionId(), e.getErrorMessage());
                }
            })
            .map(NoteStatusDto::toStringJson)
            .map(session::textMessage))
            .onErrorContinue((throwable, o) -> log.error("Exception happened!", throwable));
    }

    private Mono<Void> receiver(WebSocketSession session, User user) {
        return session.receive()
            .filter(m -> TEXT.equals(m.getType()))
            .map(WebSocketMessage::getPayloadAsText)
            .map(IncomingNoteRequest::fromStringJson)
            .map(ne -> IncomingNote.builder()
                .content(ne.getContent())
                .transactionId(ne.getTransactionId())
                .userId(user.getId())
                .build())
            .doOnNext(eventPublisher::publishEvent)
            .onErrorContinue((throwable, o) -> log.error("Exception happened!", throwable))
            .then();
    }
}
