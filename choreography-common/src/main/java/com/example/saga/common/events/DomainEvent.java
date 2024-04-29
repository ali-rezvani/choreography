package com.example.saga.common.events;

import java.time.Instant;

public interface DomainEvent {
    Instant createdAt();
}
