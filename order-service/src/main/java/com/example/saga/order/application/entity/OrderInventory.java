package com.example.saga.order.application.entity;

import com.example.saga.common.events.inventory.InventoryStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderInventory {
    @Id
    private Integer id;
    private UUID orderId;
    private UUID inventoryId;
    private InventoryStatus status;
    private String message;
    private Boolean success;
}
