package com.github.noteitdown.note.websocket;

import com.github.noteitdown.note.message.event.NoteMessageSentInternalEvent;
import com.github.noteitdown.note.model.User;
import com.github.noteitdown.note.websocket.event.WsNoteEvent;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import com.github.noteitdown.note.websocket.event.WsNoteStatusEvent;
import org.springframework.context.event.EventListener;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.DirectProcessor;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import static org.springframework.web.reactive.socket.WebSocketMessage.Type.TEXT;

public class NoteWebSocketHandler implements WebSocketHandler {

    private final DirectProcessor<NoteMessageSentInternalEvent> noteStatusEventPublisher = DirectProcessor.create();

    private final UnicastProcessor<WsNoteEventWrapper> noteEventPublisher;

    public NoteWebSocketHandler(UnicastProcessor<WsNoteEventWrapper> noteEventPublisher) {
        this.noteEventPublisher = noteEventPublisher;
    }

    @Override
    public Mono<Void> handle(WebSocketSession session) {
        User user = User.fromUri(session.getHandshakeInfo().getUri());
        return Mono.zip(receiver(session, user), sender(session, user)).then();
    }

    private Mono<Void> sender(WebSocketSession session, User user) {

        return session.send(noteStatusEventPublisher
                .map(NoteMessageSentInternalEvent::getWsNoteEventWrapper)
                .filter(e -> e.getUser().equals(user))
                .map(e -> WsNoteStatusEvent.successfulEvent(e.getWsNoteEvent()))
                .map(WsNoteStatusEvent::toStringJson)
                .map(session::textMessage))
                .onErrorContinue((throwable, o) -> System.out.println(throwable));
    }

    private Mono<Void> receiver(WebSocketSession session, User user) {
        return session.receive()
                .filter(m -> TEXT.equals(m.getType()))
                .map(WebSocketMessage::getPayloadAsText)
                .map(WsNoteEvent::fromStringJson)
                .map(ne -> new WsNoteEventWrapper(user, ne))
                .doOnNext(noteEventPublisher::onNext)
                .onErrorContinue((throwable, o) -> System.out.println(throwable))
                .then();
    }

    @EventListener
    public void onEvent(NoteMessageSentInternalEvent event) {
        noteStatusEventPublisher.onNext(event);
    }
}
