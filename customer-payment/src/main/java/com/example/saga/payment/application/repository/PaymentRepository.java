package com.example.saga.payment.application.repository;

import com.example.saga.payment.application.entity.CustomerPayment;
import com.example.saga.common.events.payment.PaymentStatus;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

import java.util.UUID;

@Repository
public interface PaymentRepository extends ReactiveCrudRepository<CustomerPayment, UUID> {
    Mono<Boolean> existsByOrderId(UUID orderId);
    Mono<CustomerPayment> findCustomerPaymentByOrOrderIdAndStatus(UUID paymentId, PaymentStatus status);
}
