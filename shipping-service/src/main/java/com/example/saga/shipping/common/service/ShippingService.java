package com.example.saga.shipping.common.service;

import com.example.saga.shipping.common.dto.ScheduleRequest;
import com.example.saga.shipping.common.dto.ShipmentDto;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface ShippingService {
    Mono<Void> addShipment(ScheduleRequest request);
    Mono<Void> cancelShipment(UUID orderId);
    Mono<ShipmentDto> scheduleShipment(UUID orderId);
}
