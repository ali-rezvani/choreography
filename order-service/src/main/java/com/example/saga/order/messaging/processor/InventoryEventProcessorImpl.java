package com.example.saga.order.messaging.processor;


import com.example.saga.common.events.inventory.InventoryEvent;
import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.processor.InventoryEventProcessor;
import com.example.saga.order.common.service.OrderFulfillmentService;
import com.example.saga.order.common.service.inventory.InventoryComponentStatusListener;
import com.example.saga.order.messaging.mapper.InventoryEventMapper;
import com.example.saga.order.messaging.mapper.OrderEventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class InventoryEventProcessorImpl implements InventoryEventProcessor<OrderEvent> {
    private final OrderFulfillmentService fulfillmentService;
    private final InventoryComponentStatusListener componentStatusListener;
    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeclined event) {
        var dto= InventoryEventMapper.toDto(event);
        return this.componentStatusListener.onSuccess(dto)
                .then(this.fulfillmentService.complete(event.orderId()))
                .map(OrderEventMapper::toOrderCompletedEvent);
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryRestored event) {
        var dto=InventoryEventMapper.toDto(event);
        return this.componentStatusListener.onRollback(dto)
                .then(Mono.empty());
    }

    @Override
    public Mono<OrderEvent> handle(InventoryEvent.InventoryDeducted event) {
        var dto= InventoryEventMapper.toDto(event);
        return this.componentStatusListener.onFailure(dto)
                .then(this.fulfillmentService.cancel(event.orderId()))
                .map(OrderEventMapper::toOrderCancelledEvent);
    }
}
