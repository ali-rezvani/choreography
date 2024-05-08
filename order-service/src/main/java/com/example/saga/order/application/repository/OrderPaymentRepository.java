package com.example.saga.order.application.repository;

import com.example.saga.order.application.entity.OrderPayment;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface OrderPaymentRepository extends ReactiveCrudRepository<OrderPayment,Integer> {
    Mono<OrderPayment> findByOrderId(UUID orderId);
}
