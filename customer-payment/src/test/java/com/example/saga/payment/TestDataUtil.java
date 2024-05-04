package com.example.saga.payment;

import com.example.saga.common.events.order.OrderEvent;

import java.time.Instant;
import java.util.UUID;

public class TestDataUtil {

    public static OrderEvent.OrderCreated createOrderCreatedEvent(int customerId,int productId,
                                                                  int unitPrice,int quantity){
        return OrderEvent.OrderCreated.builder()
                .customerId(customerId)
                .productId(productId)
                .unitPrice(unitPrice)
                .quantity(quantity)
                .orderId(UUID.randomUUID())
                .totalAmount(unitPrice*quantity)
                .createdAt(Instant.now())
                .build();
    }

    public static OrderEvent.OrderCancelled createOrderCanceledEvent(UUID orderId){
        return OrderEvent.OrderCancelled.builder()
                .orderId(orderId)
                .createdAt(Instant.now())
                .build();
    }
}
