package com.github.noteitdown.note.configuration;

import com.github.noteitdown.note.validator.NoteValidator;
import com.github.noteitdown.note.websocket.NoteWebSocketHandler;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.UnicastProcessor;

import java.util.Map;

@Configuration
public class NoteConfiguration {

    private final UnicastProcessor<WsNoteEventWrapper> wsNoteEventPublisher = UnicastProcessor.create();

    @Bean
    public HandlerMapping handlerMapping(NoteWebSocketHandler webSocketHandler) {
        var socketHandlerMap = Map.of("/websocket/note", webSocketHandler);
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(socketHandlerMap);
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return mapping;
    }

    @Bean
    public NoteWebSocketHandler getWebsocketHandler() {
        return new NoteWebSocketHandler(wsNoteEventPublisher);
    }

    @Bean
    public NoteValidator noteProcessor(ApplicationEventPublisher applicationEventPublisher) {
        return new NoteValidator(wsNoteEventPublisher, applicationEventPublisher);
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
