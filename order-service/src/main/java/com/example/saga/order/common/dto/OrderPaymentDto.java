package com.example.saga.order.common.dto;

import com.example.saga.common.events.payment.PaymentStatus;
import lombok.Builder;

import java.util.UUID;

@Builder
public record OrderPaymentDto(UUID orderId,
                              UUID paymentId,
                              PaymentStatus status,
                              String message) {
}
