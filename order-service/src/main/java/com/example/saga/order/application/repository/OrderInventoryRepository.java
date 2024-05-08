package com.example.saga.order.application.repository;

import com.example.saga.order.application.entity.OrderInventory;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderInventoryRepository extends ReactiveCrudRepository<OrderInventory,Integer> {
    Mono<OrderInventory> findByOrderId(UUID orderId);
}
