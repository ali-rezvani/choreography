package com.example.saga.common.events.order;

import com.example.saga.common.events.DomainEvent;
import com.example.saga.common.events.OrderSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public sealed interface OrderEvent extends DomainEvent, OrderSaga {

    @Builder
    record OrderCreated(UUID orderId,
                             Integer productId,
                             Integer customerId,
                             Integer quantity,
                             Integer unitPrice,
                             Integer totalAmount,
                             Instant createdAt) implements OrderEvent{
    }

    @Builder
    record OrderCancelled(UUID orderId,
                        String message,
                        Instant createdAt) implements OrderEvent{
    }

    @Builder
    record OrderCompleted(UUID orderId,
                          Instant createdAt) implements OrderEvent{
    }

}
