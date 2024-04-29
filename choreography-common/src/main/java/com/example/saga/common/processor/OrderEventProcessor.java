package com.example.saga.common.processor;

import com.example.saga.common.demo.MessageHandler;
import com.example.saga.common.events.DomainEvent;
import com.example.saga.common.events.order.OrderEvent;
import reactor.core.publisher.Mono;

public interface OrderEventProcessor<R extends DomainEvent> extends EventProcessor<OrderEvent,R> {
    @Override
    default Mono<R> process(OrderEvent event) {
        /*return MessageHandler
                .<OrderEvent,Mono<R>>create(event)
                .onMessage(OrderEvent.OrderCreated.class,this::handle)
                .onMessage(OrderEvent.OrderCancelled.class,this::handle)
                .onMessage(OrderEvent.OrderCompleted.class,this::handle)
                .handle();*/
        return switch (event){
            case OrderEvent.OrderCreated e-> this.handle(e);
            case OrderEvent.OrderCancelled e-> this.handle(e);
            case OrderEvent.OrderCompleted e-> this.handle(e);
        };
    }

    Mono<R> handle(OrderEvent.OrderCreated event);
    Mono<R> handle(OrderEvent.OrderCancelled event);
    Mono<R> handle(OrderEvent.OrderCompleted event);
}
