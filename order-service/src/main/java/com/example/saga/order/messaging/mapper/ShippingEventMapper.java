package com.example.saga.order.messaging.mapper;

import com.example.saga.common.events.shipping.ShippingEvent;
import com.example.saga.order.common.dto.OrderShipmentSchedule;

public class ShippingEventMapper {
    public static OrderShipmentSchedule toDto(ShippingEvent.ShippingScheduled event){
        return OrderShipmentSchedule.builder()
                .orderId(event.orderId())
                .deliveryDate(event.expectedDelivery())
                .build();
    }
}
