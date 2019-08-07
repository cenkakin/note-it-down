package com.github.noteitdown.note.websocket;

import com.github.noteitdown.note.model.User;
import com.github.noteitdown.note.websocket.event.NoteEvent;
import com.github.noteitdown.note.websocket.event.UserNoteEvent;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.publisher.UnicastProcessor;

import java.time.Duration;

import static java.util.UUID.randomUUID;

public class NoteWebSocketHandler implements WebSocketHandler {

	private final UnicastProcessor<UserNoteEvent> noteEventPublisher;

	private Flux<String> eventFlux = Flux.generate(sink -> {
		String event = randomUUID().toString();
		sink.next(event);
	});

	private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(1000L))
		.zipWith(eventFlux, (time, event) -> event);

	public NoteWebSocketHandler(UnicastProcessor<UserNoteEvent> noteEventPublisher) {
		this.noteEventPublisher = noteEventPublisher;
	}

	@Override
	public Mono<Void> handle(WebSocketSession session) {
		User user = User.fromUri(session.getHandshakeInfo().getUri());
		Mono<Void> input = session.receive()
			.filter(m -> WebSocketMessage.Type.TEXT.equals(m.getType()))
			.map(WebSocketMessage::getPayloadAsText)
			.map(NoteEvent::to)
			.map(ne -> new UserNoteEvent(user, ne))
			.doOnNext(noteEventPublisher::onNext)
			.onErrorContinue((throwable, o) -> System.out.println(throwable))
			.then();

		Mono<Void> output = session.send(intervalFlux
			.map(session::textMessage));

		return Mono.zip(input, output).onErrorContinue((throwable, o) -> System.out.println(throwable)).then();
	}
}
