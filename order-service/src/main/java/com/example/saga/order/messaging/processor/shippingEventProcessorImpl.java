package com.example.saga.order.messaging.processor;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.shipping.ShippingEvent;
import com.example.saga.common.processor.ShippingEventProcessor;
import com.example.saga.order.common.service.shipping.ShippingComponentStatusListener;
import com.example.saga.order.messaging.mapper.ShippingEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
@RequiredArgsConstructor
@Slf4j
public class shippingEventProcessorImpl implements ShippingEventProcessor<OrderEvent> {
    private final ShippingComponentStatusListener componentStatusListener;
    @Override
    public Mono<OrderEvent> handle(ShippingEvent.ShippingScheduled event) {
        var dto= ShippingEventMapper.toDto(event);
        return this.componentStatusListener.onSuccess(dto)
                .then(Mono.empty());
    }
}
