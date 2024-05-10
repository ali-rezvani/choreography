package com.example.saga.order.messaging.config;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.order.common.service.OrderEventListener;
import com.example.saga.order.messaging.publisher.OrderEventListenerImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Sinks;

@Configuration
public class OrderEventListenerConfig {
    @Bean
    public OrderEventListener orderEventListener(){
        var sink= Sinks.many().unicast().<OrderEvent>onBackpressureBuffer();
        var flux=sink.asFlux();
        return new OrderEventListenerImpl(sink,flux);
    }
}
