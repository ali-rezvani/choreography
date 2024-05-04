package com.example.saga.inventory.messaging.mapper;

import com.example.saga.common.events.inventory.InventoryEvent;
import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.inventory.common.dto.InventoryDeductRequest;
import com.example.saga.inventory.common.dto.OrderInventoryDto;
import com.example.saga.inventory.common.service.InventoryService;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.function.Function;

public class MessageDtoMapper {
    public static InventoryDeductRequest toInventoryDeductedRequest(OrderEvent.OrderCreated event) {
        return InventoryDeductRequest.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .quantity(event.quantity())
                .build();
    }

    public static InventoryEvent toInventoryDeductedEvent(OrderInventoryDto dto) {
        return InventoryEvent.InventoryDeducted.builder()
                .orderId(dto.orderId())
                .inventoryId(dto.inventoryId())
                .productId(dto.productId())
                .quantity(dto.quantity())
                .createdAt(Instant.now())
                .build();
    }

    public static InventoryEvent toInventoryRestoredEvent(OrderInventoryDto dto) {
        return InventoryEvent.InventoryRestored.builder()
                .orderId(dto.orderId())
                .inventoryId(dto.inventoryId())
                .productId(dto.productId())
                .quantity(dto.quantity())
                .createdAt(Instant.now())
                .build();
    }

    public static Function<Throwable, Mono<InventoryEvent>> toInventoryReDeclinedEvent(OrderEvent.OrderCreated event) {
        return ex -> Mono.fromSupplier(() -> InventoryEvent.InventoryDeclined.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .quantity(event.quantity())
                .createdAt(Instant.now())
                .message(ex.getMessage())
                .build());

    }
}
