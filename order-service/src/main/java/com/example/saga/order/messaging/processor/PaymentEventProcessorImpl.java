package com.example.saga.order.messaging.processor;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.payment.PaymentEvent;
import com.example.saga.common.processor.PaymentEventProcessor;
import com.example.saga.order.common.service.OrderFulfillmentService;
import com.example.saga.order.common.service.payment.PaymentComponentStatusListener;
import com.example.saga.order.messaging.mapper.OrderEventMapper;
import com.example.saga.order.messaging.mapper.PaymentEventMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
@Service
@AllArgsConstructor
@Slf4j
public class PaymentEventProcessorImpl implements PaymentEventProcessor<OrderEvent> {
    private final OrderFulfillmentService orderFulfillmentService;
    private final PaymentComponentStatusListener componentStatusListener;
    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeducted event) {
        var dto= PaymentEventMapper.toDto(event);
        return this.componentStatusListener.onSuccess(dto)
                .then(this.orderFulfillmentService.complete(event.orderId()))
                .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentRefunded event) {
        var dto=PaymentEventMapper.toDto(event);
        return this.componentStatusListener.onRollback(dto)
                .then(Mono.empty());
    }

    @Override
    public Mono<OrderEvent> handle(PaymentEvent.PaymentDeclined event) {
        var dto=PaymentEventMapper.toDto(event);
        return this.componentStatusListener.onFailure(dto)
                .then(this.orderFulfillmentService.cancel(event.orderId()))
                .map(OrderEventMapper::toOrderCancelledEvent);
    }
}
