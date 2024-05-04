package com.example.saga.inventory.aplication.repository;

import com.example.saga.common.events.inventory.InventoryStatus;
import com.example.saga.inventory.aplication.entity.OrderInventory;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderInventoryRepository  extends ReactiveCrudRepository<OrderInventory, UUID> {
    Mono<Boolean> existsByOrderId(UUID orderId);
    Mono<OrderInventory> findByOrderIdAndStatus(UUID orderId, InventoryStatus status);
}
