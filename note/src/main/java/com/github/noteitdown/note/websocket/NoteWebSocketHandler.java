package com.github.noteitdown.note.websocket;

import com.github.noteitdown.note.processor.event.NoteMessageSentInternalEvent;
import com.github.noteitdown.note.model.User;
import com.github.noteitdown.note.websocket.event.WsNoteEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import com.github.noteitdown.note.websocket.event.WsNoteStatusEvent;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import static org.springframework.web.reactive.socket.WebSocketMessage.Type.TEXT;

public class NoteWebSocketHandler implements WebSocketHandler {

    private final UnicastProcessor<WsNoteEventWrapper> noteEventPublisher;

    private final Flux<NoteMessageSentInternalEvent> processedNotePublisher;

    public NoteWebSocketHandler(UnicastProcessor<WsNoteEventWrapper> noteEventPublisher,
                                Flux<NoteMessageSentInternalEvent> processedNotePublisher) {
        this.noteEventPublisher = noteEventPublisher;
        this.processedNotePublisher = processedNotePublisher;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        User user = User.fromUri(session.getHandshakeInfo().getUri());
        return Mono.zip(receiver(session, user), sender(session, user)).then();
    }

    private Mono<Void> sender(WebSocketSession session, User user) {
        return session.send(processedNotePublisher
                .doOnNext(e -> System.out.println("BBBBBB3"))
                .map(NoteMessageSentInternalEvent::getWsNoteEventWrapper)
                .filter(e -> e.getUser().equals(user))
                .map(e -> WsNoteStatusEvent.successfulEvent(e.getWsNoteEvent()))
                .map(WsNoteStatusEvent::toStringJson)
                .map(session::textMessage))
                .onErrorContinue((throwable, o) -> System.out.println(throwable));
    }

    private Mono<Void> receiver(WebSocketSession session, User user) {
        return session.receive()
                .doOnNext(e -> System.out.println("AAAAA1"))
                .filter(m -> TEXT.equals(m.getType()))
                .map(WebSocketMessage::getPayloadAsText)
                .map(WsNoteEvent::fromStringJson)
                .map(ne -> new WsNoteEventWrapper(user, ne))
                .doOnNext(e -> {
                    System.out.println("HEREEE");
                    noteEventPublisher.onNext(e);
                } )
                .onErrorContinue((throwable, o) -> System.out.println(throwable))
                .then();
    }
}
