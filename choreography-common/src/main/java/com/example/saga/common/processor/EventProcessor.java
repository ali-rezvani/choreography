package com.example.saga.common.processor;

import com.example.saga.common.events.DomainEvent;
import reactor.core.publisher.Mono;

public interface EventProcessor<T extends DomainEvent,R extends DomainEvent> {
    Mono<R> process(T event);
}
