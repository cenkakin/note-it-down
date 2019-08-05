package com.github.noteitdown.note.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noteitdown.note.dto.NoteCommandDto;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;

import static java.util.UUID.randomUUID;

public class NoteWebSocketHandler implements WebSocketHandler {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private Flux<String> eventFlux = Flux.generate(sink -> {
        String event = randomUUID().toString();
        sink.next(event);
    });

    private Flux<String> intervalFlux = Flux.interval(Duration.ofMillis(1000L))
            .zipWith(eventFlux, (time, event) -> event);


    @Override
    public Mono<Void> handle(WebSocketSession session) {
        Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toNoteCommandDto).then();
        Mono<Void> output = session.send(intervalFlux
                .map(session::textMessage)
				.doOnNext(s -> System.out.println("Sending event: " + s)));

        return Mono.zip(input, output).then();
    }
//
//	public static void main(String[] args) {
//		URI targetUri = URI.create("http://backend-service/foo/");
//		System.out.println(targetUri.getAuthority());
//	}

    private NoteCommandDto toNoteCommandDto(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, NoteCommandDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }
}
