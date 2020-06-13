package com.github.noteitdown.note.websocket;

import com.github.noteitdown.note.model.User;
import com.github.noteitdown.note.processor.NoteProcessedEventPublisher;
import com.github.noteitdown.note.processor.event.NoteProcessedEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import com.github.noteitdown.note.websocket.event.WsNoteStatusEvent;
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
            .map(NoteProcessedEvent::getNote)
            .log()
            .filter(e -> e.getUserId().equals(user.getId()))
            .map(e -> WsNoteStatusEvent.successfulEvent(e.getTransactionId()))
            .map(WsNoteStatusEvent::toStringJson)
            .map(session::textMessage))
            .onErrorContinue((throwable, o) -> log.error("Exception happened!", throwable));
    }

    private Mono<Void> receiver(WebSocketSession session, User user) {
        return session.receive()
            .filter(m -> TEXT.equals(m.getType()))
            .map(WebSocketMessage::getPayloadAsText)
            .map(WsNoteEvent::fromStringJson)
            .map(ne -> new WsNoteEventWrapper(user, ne))
            .doOnNext(eventPublisher::publishEvent)
            .onErrorContinue((throwable, o) -> log.error("Exception happened!", throwable))
            .then();
    }
}
