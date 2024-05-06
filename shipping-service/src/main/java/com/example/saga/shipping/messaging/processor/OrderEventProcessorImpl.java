package com.example.saga.shipping.messaging.processor;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.shipping.ShippingEvent;
import com.example.saga.common.exception.EventAlreadyProcessedException;
import com.example.saga.common.processor.OrderEventProcessor;
import com.example.saga.shipping.common.service.ShippingService;
import com.example.saga.shipping.messaging.mapper.MessageDtoMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.function.UnaryOperator;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderEventProcessorImpl implements OrderEventProcessor<ShippingEvent> {
    private final ShippingService shippingService;

    @Override
    public Mono<ShippingEvent> handle(OrderEvent.OrderCreated event) {
        return this.shippingService.addShipment(MessageDtoMapper.toScheduleRequest(event))
                .transform(exceptionHandler())
                .then(Mono.empty());
    }

    @Override
    public Mono<ShippingEvent> handle(OrderEvent.OrderCancelled event) {
        return this.shippingService.cancelShipment(event.orderId())
                .then(Mono.empty());
    }

    @Override
    public Mono<ShippingEvent> handle(OrderEvent.OrderCompleted event) {
        return this.shippingService.scheduleShipment(event.orderId())
                .map(MessageDtoMapper::toShippingScheduledEvent)
                .doOnNext(e->log.info("Shipping scheduled completely {}",e));
    }

    private <T> UnaryOperator<Mono<T>> exceptionHandler() {
        return mono -> mono
                .onErrorResume(EventAlreadyProcessedException.class, ex -> Mono.empty())
                .doOnError(ex -> log.error("Exception:{}", ex.getMessage()));
    }
}
