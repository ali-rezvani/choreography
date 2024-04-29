package com.example.saga.common.service;

import com.example.saga.common.dto.PaymentDto;
import com.example.saga.common.dto.PaymentProcessRequest;
import reactor.core.publisher.Mono;

import java.util.UUID;

public interface PaymentService {
    Mono<PaymentDto> process(PaymentProcessRequest request);
    Mono<PaymentDto> refund(UUID orderId);
}
