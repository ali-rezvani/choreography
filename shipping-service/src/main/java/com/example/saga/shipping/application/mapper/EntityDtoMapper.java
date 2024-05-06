package com.example.saga.shipping.application.mapper;

import com.example.saga.shipping.application.entity.Shipment;
import com.example.saga.shipping.common.dto.ScheduleRequest;
import com.example.saga.shipping.common.dto.ShipmentDto;

public class EntityDtoMapper {
    public static Shipment toShipment(ScheduleRequest request){
        return Shipment.builder()
                .orderId(request.orderId())
                .productId(request.productId())
                .customerId(request.customerId())
                .quantity(request.quantity())
                .build();
    }

    public static ShipmentDto toShipmentDto(Shipment entity){
        return ShipmentDto.builder()
                .shipmentId(entity.getId())
                .orderId(entity.getOrderId())
                .productId(entity.getProductId())
                .customerId(entity.getCustomerId())
                .quantity(entity.getQuantity())
                .expectedDate(entity.getDeliveryDate())
                .status(entity.getStatus())
                .build();
    }
}
