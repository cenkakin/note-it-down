package com.github.noteitdown.note.configuration;

import com.github.noteitdown.note.processor.NoteProcessor;
import com.github.noteitdown.note.websocket.NoteWebSocketHandler;
import com.github.noteitdown.note.websocket.event.UserNoteEvent;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.WebSocketHandler;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.UnicastProcessor;

import java.util.Map;

@Configuration
public class NoteConfiguration {

	@Bean
	public UnicastProcessor<UserNoteEvent> messagePublisher() {
		return UnicastProcessor.create();
	}

	@Bean
	public HandlerMapping handlerMapping(UnicastProcessor<UserNoteEvent> noteEventPublisher) {
		Map<String, WebSocketHandler> map = Map.of("/websocket/note", new NoteWebSocketHandler(noteEventPublisher));
		SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
		mapping.setUrlMap(map);
		mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
		return mapping;
	}

	@Bean
	public NoteProcessor noteProcessor(UnicastProcessor<UserNoteEvent> noteEventPublisher) {
		return new NoteProcessor(noteEventPublisher);
	}

	@Bean
	public WebSocketHandlerAdapter handlerAdapter() {
		return new WebSocketHandlerAdapter();
	}
}
