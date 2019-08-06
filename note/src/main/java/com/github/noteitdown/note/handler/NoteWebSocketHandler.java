package com.github.noteitdown.note.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noteitdown.note.dto.NoteCommandDto;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.WebSocketMessage;
import org.springframework.web.reactive.socket.WebSocketSession;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.web.util.UriBuilder;
import org.springframework.web.util.UriBuilderFactory;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.CompletableFuture;

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
		String subject = UriComponentsBuilder.fromUri(session.getHandshakeInfo().getUri())
			.build().getQueryParams().getFirst("subject");

		Mono<Void> input = session.receive()
                .map(WebSocketMessage::getPayloadAsText)
                .map(this::toNoteCommandDto).then();
        Mono<Void> output = session.send(intervalFlux
                .map(session::textMessage)
				.doOnNext(s -> System.out.println("Sending event: " + s)));

        return Mono.zip(input, output).then();
    }

    private NoteCommandDto toNoteCommandDto(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, NoteCommandDto.class);
        } catch (IOException e) {
            throw new RuntimeException("Invalid JSON:" + json, e);
        }
    }
}
