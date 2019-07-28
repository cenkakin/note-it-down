package com.github.noteitdown.common.event;

import java.time.Instant;

/**
 * Created by cenkakin
 */
public class KafkaEvent<T> {

    private final T payload;

    private final Long version;

    private final Instant createdAt;

    private final String eventType;

    public KafkaEvent(T payload, Long version, String eventType) {
        this.payload = payload;
        this.version = version;
        this.eventType = eventType;
        this.createdAt = Instant.now();
    }

    public T getPayload() {
        return payload;
    }

    public Long getVersion() {
        return version;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public String getEventType() {
        return eventType;
    }
}
