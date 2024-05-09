package com.example.saga.order.messaging.publisher;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.publisher.EventPublisher;
import com.example.saga.order.common.dto.PurchaseOrderDto;
import com.example.saga.order.common.service.OrderEventListener;
import com.example.saga.order.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.time.Duration;

@RequiredArgsConstructor
public class OrderEventListenerImpl implements OrderEventListener, EventPublisher<OrderEvent> {
    private final Sinks.Many<OrderEvent> sink;
    private final Flux<OrderEvent> flux;

    @Override
    public void emitOrderCreated(PurchaseOrderDto dto) {
        var event = OrderEventMapper.toOrderCreatedEvent(dto);
        this.sink.emitNext(
                event,
                Sinks.EmitFailureHandler.busyLooping(Duration.ofSeconds(1))
        );
    }

    @Override
    public Flux<OrderEvent> publish() {
        return this.flux;
    }
}
