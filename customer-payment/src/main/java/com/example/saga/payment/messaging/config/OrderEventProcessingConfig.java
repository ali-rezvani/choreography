package com.example.saga.payment.messaging.config;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.payment.PaymentEvent;
import com.example.saga.common.processor.OrderEventProcessor;
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
public class OrderEventProcessingConfig {
    private final OrderEventProcessor<PaymentEvent> eventProcessor;

    @Bean
    public Function<Flux<Message<OrderEvent>>,Flux<Message<PaymentEvent>>> processor(){
        return flux->flux
                .map(MessageConverter::convertToRecord)
                .doOnNext(r->log.info("customer payment received {}",r.message()))
                .concatMap(r->this.eventProcessor.process(r.message())
//                        .retry(2)
//                        .onErrorResume()
                        .doOnSuccess(e->r.acknowledgment().acknowledge())
                )
                .map(this::toMessage);
    }

    private Message<PaymentEvent> toMessage(PaymentEvent event){
        return MessageBuilder
                .withPayload(event)
                .setHeader(KafkaHeaders.KEY,event.orderId().toString())
                .build();
    }
}
