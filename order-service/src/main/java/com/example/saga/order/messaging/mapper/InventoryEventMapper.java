package com.example.saga.order.messaging.mapper;

import com.example.saga.common.events.inventory.InventoryEvent;
import com.example.saga.common.events.inventory.InventoryStatus;
import com.example.saga.order.common.dto.OrderInventoryDto;

public class InventoryEventMapper {
    public static OrderInventoryDto toDto(InventoryEvent.InventoryDeducted event) {
        return OrderInventoryDto.builder()
                .orderId(event.orderId())
                .inventoryId(event.orderId())
                .status(InventoryStatus.DEDUCTED)
                .build();
    }

    public static OrderInventoryDto toDto(InventoryEvent.InventoryRestored event) {
        return OrderInventoryDto.builder()
                .orderId(event.orderId())
                .status(InventoryStatus.RESTORED)
                .build();
    }

    public static OrderInventoryDto toDto(InventoryEvent.InventoryDeclined event) {
        return OrderInventoryDto.builder()
                .orderId(event.orderId())
                .status(InventoryStatus.DEDUCTED)
                .message(event.message())
                .build();
    }
}
