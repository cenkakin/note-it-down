package com.github.noteitdown.note.domain.note;

import com.github.noteitdown.note.domain.note.event.NoteProcessedEvent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Consumer;
import org.springframework.context.event.EventListener;
import org.springframework.util.ReflectionUtils;
import reactor.core.publisher.FluxSink;

public class NoteProcessedEventPublisher implements Consumer<FluxSink<NoteProcessedEvent>> {

    private final Executor executor;
    private final BlockingQueue<NoteProcessedEvent> queue = new LinkedBlockingQueue<>();

    public NoteProcessedEventPublisher(Executor executor) {
        this.executor = executor;
    }

    @EventListener(NoteProcessedEvent.class)
    public void onApplicationEvent(NoteProcessedEvent event) {
        this.queue.offer(event);
    }

    @Override
    public void accept(FluxSink<NoteProcessedEvent> sink) {
        this.executor.execute(() -> {
            while (true) {
                try {
                    NoteProcessedEvent event = queue.take();
                    sink.next(event);
                } catch (InterruptedException e) {
                    ReflectionUtils.rethrowRuntimeException(e);
                }
            }
        });
    }
}
