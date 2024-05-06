package com.example.saga.shipping.messaging.mapper;

import com.example.saga.common.events.order.OrderEvent;
import com.example.saga.common.events.shipping.ShippingEvent;
import com.example.saga.shipping.common.dto.ScheduleRequest;
import com.example.saga.shipping.common.dto.ShipmentDto;

import java.time.Instant;

public class MessageDtoMapper {
    public static ScheduleRequest toScheduleRequest(OrderEvent.OrderCreated event){
        return ScheduleRequest.builder()
                .orderId(event.orderId())
                .productId(event.productId())
                .customerId(event.customerId())
                .quantity(event.quantity())
                .build();
    }

    public static ShippingEvent toShippingScheduledEvent(ShipmentDto dto){
        return ShippingEvent.ShippingScheduled.builder()
                .shipmentId(dto.shipmentId())
                .orderId(dto.orderId())
                .expectedDelivery(dto.expectedDate())
                .createdAt(Instant.now())
                .build();
    }
}
