package com.example.saga.common.publisher;

import com.example.saga.common.events.DomainEvent;
import reactor.core.publisher.Flux;

public interface EventPublisher<T extends DomainEvent> {
    Flux<T> publish();
}
