package com.example.saga.inventory.messaging.config;

import com.example.saga.common.events.inventory.InventoryEvent;
import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.processor.OrderEventProcessor;
import com.example.saga.common.util.MessageConverter;
import com.example.saga.inventory.messaging.processor.OrderEventProcessorImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import reactor.core.publisher.Flux;

import java.util.function.Function;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class OrderEventProcessorConfig {
    private final OrderEventProcessor<InventoryEvent> orderEventProcessor;

    @Bean
    public Function<Flux<Message<OrderEvent>>, Flux<Message<InventoryEvent>>> processor() {
        return flux -> flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(r -> log.info("inventory service received {}", r.message()))
                .concatMap(r -> this.orderEventProcessor.process(r.message())
                        .doOnSuccess(e -> r.acknowledgment().acknowledge()))
                .map(this::toMessage);
    }

    private Message<InventoryEvent> toMessage(InventoryEvent event){
        return MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.KEY,event.orderId().toString())
                .build();
    }
}
