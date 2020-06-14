package com.github.noteitdown.note.configuration;

import com.github.noteitdown.note.application.websocket.NoteWebSocketHandler;
import com.github.noteitdown.note.domain.note.IncomingNoteProcessor;
import com.github.noteitdown.note.domain.note.NoteProcessedEventPublisher;
import com.github.noteitdown.note.domain.note.repository.NoteRepository;
import com.github.noteitdown.note.domain.note.service.NoteService;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;

@Configuration
public class NoteConfiguration {

    @Bean
    public Executor executor() {
        return Executors.newSingleThreadExecutor();
    }

    @Bean
    public NoteWebSocketHandler webSocketHandler(ApplicationEventPublisher eventPublisher, NoteProcessedEventPublisher noteProcessedEventPublisher) {
        return new NoteWebSocketHandler(eventPublisher, noteProcessedEventPublisher);
    }

    @Bean
    public HandlerMapping handlerMapping(NoteWebSocketHandler webSocketHandler) {
        var socketHandlerMap = Map.of("/websocket/note", webSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(socketHandlerMap);
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return mapping;
    }

    @Bean
    public NoteProcessedEventPublisher noteProcessedEventPublisher(Executor executor) {
        return new NoteProcessedEventPublisher(executor);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }

    @Bean
    public NoteService noteService(NoteRepository noteRepository) {
        return new NoteService(noteRepository);
    }

    @Bean
    public IncomingNoteProcessor noteProcessor(ApplicationEventPublisher applicationEventPublisher, NoteService noteService) {
        return new IncomingNoteProcessor(applicationEventPublisher, noteService);
    }
}
