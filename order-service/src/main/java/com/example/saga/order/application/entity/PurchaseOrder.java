package com.example.saga.order.application.entity;

import com.example.saga.common.events.order.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.time.Instant;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PurchaseOrder {
    @Id
    private UUID orderId;
    private Integer customerId;
    private Integer productId;
    private Integer quantity;
    private Integer unitPrice;
    private Integer amount;
    private OrderStatus status;
    private Instant deliveryDate;
}
