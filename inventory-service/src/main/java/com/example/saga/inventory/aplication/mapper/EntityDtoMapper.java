package com.example.saga.inventory.aplication.mapper;

import com.example.saga.inventory.aplication.entity.OrderInventory;
import com.example.saga.inventory.common.dto.InventoryDeductRequest;
import com.example.saga.inventory.common.dto.OrderInventoryDto;

public class EntityDtoMapper {
    public static OrderInventory toOrderInventory(InventoryDeductRequest request){
        return OrderInventory.builder()
                .orderId(request.orderId())
                .productId(request.productId())
                .quantity(request.quantity())
                .build();
    }

    public static OrderInventoryDto toOrderInventoryDto(OrderInventory orderInventory){
        return OrderInventoryDto.builder()
                .inventoryId(orderInventory.getInventoryId())
                .orderId(orderInventory.getOrderId())
                .productId(orderInventory.getProductId())
                .quantity(orderInventory.getQuantity())
                .status(orderInventory.getStatus())
                .build();
    }
}
