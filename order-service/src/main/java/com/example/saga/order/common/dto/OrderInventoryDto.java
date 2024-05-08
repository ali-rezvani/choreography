package com.example.saga.order.common.dto;

import com.example.saga.common.events.inventory.InventoryStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderInventoryDto(UUID orderId,
                                UUID inventoryId,
                                InventoryStatus status,
                                String message) {
}
