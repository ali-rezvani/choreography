package com.example.saga.application.repository;

import com.example.saga.application.entity.CustomerPayment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<CustomerPayment, UUID> {
    Mono<Boolean> existsByOrderId(UUID orderId);
}
