package com.example.saga.order.messaging.config;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.payment.PaymentEvent;
import com.example.saga.common.processor.PaymentEventProcessor;
import com.example.saga.common.util.MessageConverter;
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
public class PaymentEventProcessingConfig {
    private final PaymentEventProcessor<OrderEvent> eventProcessor;

    @Bean
    public Function<Flux<Message<PaymentEvent>>,Flux<Message<OrderEvent>>> processor(){
        return flux->flux.map(MessageConverter::convertToRecord)
                .doOnNext(r->log.info("order service received {}",r))
                .concatMap(r->this.eventProcessor.process(r.message())
                        .doOnSuccess(e->r.acknowledgment().acknowledge())
                )
                .map(this::toMessage);
    }
    private Message<OrderEvent> toMessage(OrderEvent event){
        return MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.KEY,event.orderId().toString())
                .build();
    }
}
