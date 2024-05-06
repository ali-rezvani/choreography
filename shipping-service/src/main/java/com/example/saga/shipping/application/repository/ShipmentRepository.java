package com.example.saga.shipping.application.repository;

import com.example.saga.common.events.shipping.ShippingStatus;
import com.example.saga.shipping.application.entity.Shipment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface ShipmentRepository extends ReactiveCrudRepository<Shipment, UUID> {
    Mono<Boolean> existsByOrderId(UUID orderId);
    Mono<Shipment> findByOrderIdAndStatus(UUID orderId, ShippingStatus status);
    Mono<Void> deleteByOrderId(UUID orderId);
}
