package com.example.saga.order.messaging.config;

import com.example.saga.common.events.DomainEvent;
import com.example.saga.common.events.inventory.InventoryEvent;
import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.processor.EventProcessor;
import com.example.saga.common.util.MessageConverter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Slf4j
public abstract class AbstractOrderEventRouterConfig {
    public static final String DESTINATION_HEADER="spring.cloud.stream.sendto.destination";
    public static final String ORDER_EVENTS_CHANNEL="order-events-channel";

    protected  <T extends DomainEvent>Function<Flux<Message<T>>,Flux<Message<OrderEvent>>> processor(EventProcessor<T,OrderEvent> eventProcessor){
        return flux->flux.map(MessageConverter::convertToRecord)
                .doOnNext(r->log.info("order service received {}",r))
                .concatMap(r->eventProcessor.process(r.message())
                        .doOnSuccess(e->r.acknowledgment().acknowledge())
                )
                .map(this::toMessage);
    }
    protected Message<OrderEvent> toMessage(OrderEvent event){
        log.info("Order service produced {}",event);
        return MessageBuilder
                .withPayload(event)
                .setHeader(DESTINATION_HEADER,ORDER_EVENTS_CHANNEL)
                .build();
    }

}
