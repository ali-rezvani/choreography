package com.example.saga.common.processor;

import com.example.saga.common.events.DomainEvent;
import com.example.saga.common.events.inventory.InventoryEvent;
import reactor.core.publisher.Mono;

public interface InventoryEventProcessor<R extends DomainEvent> extends EventProcessor<InventoryEvent,R> {
    @Override
    default Mono<R> process(InventoryEvent event) {
        return switch (event){
            case InventoryEvent.InventoryDeclined e-> this.handle(e);
            case InventoryEvent.InventoryRestored e-> this.handle(e);
            case InventoryEvent.InventoryDeducted e-> this.handle(e);
        };
    }

    Mono<R> handle(InventoryEvent.InventoryDeclined event);
    Mono<R> handle(InventoryEvent.InventoryRestored event);
    Mono<R> handle(InventoryEvent.InventoryDeducted event);
}
