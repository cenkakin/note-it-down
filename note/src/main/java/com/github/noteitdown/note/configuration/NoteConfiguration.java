package com.github.noteitdown.note.configuration;

import com.github.noteitdown.note.processor.ProcessedNotePublisher;
import com.github.noteitdown.note.processor.event.NoteMessageSentInternalEvent;
import com.github.noteitdown.note.websocket.NoteWebSocketHandler;
import com.github.noteitdown.note.websocket.event.WsNoteEventWrapper;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.web.reactive.HandlerMapping;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import org.springframework.web.reactive.socket.server.support.WebSocketHandlerAdapter;
import reactor.core.publisher.Flux;
import reactor.core.publisher.UnicastProcessor;

import java.util.Map;

@Configuration
@EnableBinding(Source.class)
public class NoteConfiguration {

    private final UnicastProcessor<WsNoteEventWrapper> wsNoteEventPublisher = UnicastProcessor.create();

    @Bean
    public HandlerMapping handlerMapping(Source messageBroker) {
        var socketHandlerMap = Map.of("/websocket/note", new NoteWebSocketHandler(wsNoteEventPublisher,
                ProcessedNotePublisher.apply(messageBroker, wsNoteEventPublisher)));
        SimpleUrlHandlerMapping mapping = new SimpleUrlHandlerMapping();
        mapping.setUrlMap(socketHandlerMap);
        mapping.setOrder(Ordered.HIGHEST_PRECEDENCE + 1);
        return mapping;
    }

    @Bean
    public WebSocketHandlerAdapter handlerAdapter() {
        return new WebSocketHandlerAdapter();
    }
}
