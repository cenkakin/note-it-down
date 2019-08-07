package com.github.noteitdown.note.processor;

import com.github.noteitdown.note.websocket.event.UserNoteEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

import javax.annotation.PostConstruct;

public class NoteProcessor {

	private static final Logger LOG = LoggerFactory.getLogger(NoteProcessor.class);

	private final Flux<UserNoteEvent> noteEventPublisher;

	public NoteProcessor(Flux<UserNoteEvent> noteEventPublisher) {
		this.noteEventPublisher = noteEventPublisher
			.replay(25)
			.autoConnect();
	}

	@PostConstruct
	public void init() {
		process();
	}

	private void process() {
		noteEventPublisher
			.log()
			.subscribe();
	}
}
