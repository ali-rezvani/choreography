package com.example.saga.common.events.inventory;

import com.example.saga.common.events.DomainEvent;
import com.example.saga.common.events.OrderSaga;
import lombok.Builder;

import java.time.Instant;
import java.util.UUID;

public sealed interface InventoryEvent extends DomainEvent, OrderSaga {

    @Builder
    record InventoryDeducted(UUID orderId,
                             UUID inventoryId,
                             Integer productId,
                             Integer quantity,
                             Instant createdAt) implements InventoryEvent{

    }

    @Builder
    record InventoryRestored(UUID orderId,
                             UUID inventoryId,
                             Integer productId,
                             Integer quantity,
                             Instant createdAt) implements InventoryEvent{

    }

    @Builder
    record InventoryDeclined(UUID orderId,
                             Integer productId,
                             Integer quantity,
                             String message,
                             Instant createdAt) implements InventoryEvent{

    }
}
